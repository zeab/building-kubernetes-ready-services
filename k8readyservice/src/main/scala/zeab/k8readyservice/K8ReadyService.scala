package zeab.k8readyservice

//Imports
import java.util.Properties

import com.datastax.driver.core.{Cluster, Session}
import org.apache.kafka.clients.consumer.{ConsumerRecord, KafkaConsumer}
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.zookeeper.ZooKeeper
import zeab.k8readyservice.httpservice.Routes
import zeab.k8readyservice.zookeeper.StubWatcher
import zeab.scalaextras.logging.Logging
//Akka
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.typesafe.config.ConfigFactory
//Scala
import scala.collection.JavaConverters._
import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

object K8ReadyService extends App with Logging {

  //Akka
  implicit val system: ActorSystem = ActorSystem("StandardService", ConfigFactory.load())
  implicit val mat: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher

  //Cassandra
  val cassandraHost: String = getEnvVar[String]("CASSANDRA_HOST", "192.168.1.144")
  //val cassandraPort: Int = getEnvVar[Int]("CASSANDRA_PORT", 0)
  val cassandraUsername: String = getEnvVar[String]("CASSANDRA_USERNAME", "")
  val cassandraPassword: String = getEnvVar[String]("CASSANDRA_PASSWORD", "")
  implicit val cluster: Cluster =
    if (cassandraPassword != "" && cassandraUsername != "")
      Cluster.builder
        .addContactPoint(cassandraHost)
        .withCredentials(cassandraUsername, cassandraPassword)
        .build
    else
      Cluster.builder
        .addContactPoint(cassandraHost)
        .build

  //TODO put this back properly...
  implicit val session: Session = cluster.connect()

  val zooKeeperHost: String = getEnvVar[String]("ZOOKEEPER_HOST", "localhost")
  val zooKeeperPort: String = getEnvVar[String]("ZOOKEEPER_PORT", "2181")
  implicit val zooKeeper: ZooKeeper = new ZooKeeper(s"$zooKeeperHost:$zooKeeperPort", 10000, new StubWatcher)

  //Kafka
  val kafkaHost: String = getEnvVar[String]("KAFKA_HOST", "localhost")
  val kafkaPort: String = getEnvVar[String]("KAFKA_PORT", "9092")
  val kafkaConsumerGroupId: String = getEnvVar[String]("KAFKA_CONSUMER_GROUP_ID", "my-consumer-id")
  val kafkaConsumerGroups: List[String] = getEnvVar[String]("KAFKA_CONSUMER_GROUPS", ',', List("my-topic"))
  //Kafka - Producer
  val producerProps: Properties = new Properties()
  producerProps.put("bootstrap.servers", s"$kafkaHost:$kafkaPort")
  producerProps.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  producerProps.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  implicit val producer: KafkaProducer[String, String] = new KafkaProducer[String, String](producerProps)
  log.info("Kafka Producer open")
  //Kafka - Consumer
  val consumerProps: Properties = new Properties()
  consumerProps.put("bootstrap.servers", s"$kafkaHost:$kafkaPort")
  consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  consumerProps.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  consumerProps.put("group.id", kafkaConsumerGroupId)
  implicit val consumer: KafkaConsumer[String, String] =
    new KafkaConsumer[String, String](consumerProps)
  consumer.subscribe(kafkaConsumerGroups.asJavaCollection)
  val consumerPoll: Future[Unit] =
    Future {
      while (true) {
        val records: Iterable[ConsumerRecord[String, String]] =
          consumer.poll(1.second.toMillis).asScala
        records.foreach { msg => system.eventStream.publish(msg) }
      }
    }

  //Http Server
  val httpServiceHost: String = getEnvVar[String]("HTTP_SERVICE_HOST", "0.0.0.0")
  val httpServicePort: Int = getEnvVar[Int]("HTTP_SERVICE_PORT", 8080)
  val httpServerSource: Source[Http.IncomingConnection, Future[Http.ServerBinding]] =
    Http().bind(interface = httpServiceHost, httpServicePort)
  implicit val httpBinding: Future[Http.ServerBinding] =
    httpServerSource.to(Sink.foreach { connection =>
      log.debug("Accepted new connection from {}", connection.remoteAddress)
      connection.handleWith(Routes.all)
    }).run()
  log.info(s"Http Server is now online at http://$httpServiceHost:$httpServicePort")

  //On init of shutdown... do this...
  scala.sys.addShutdownHook {
    httpBinding.onComplete { _ =>
      log.info("Http Server is now offline")
      //TODO Add a graceful shutdown to clear messages before shutting everything down
      //something like shutdown the http service and then keep checking the kafka stream... if its still processing... wait ... else shutdown kafka and cassandra
      log.info("Terminated... Exiting")
      mat.shutdown()
      system.terminate()
      Await.result(system.whenTerminated, 30.seconds)
    }
  }

}

