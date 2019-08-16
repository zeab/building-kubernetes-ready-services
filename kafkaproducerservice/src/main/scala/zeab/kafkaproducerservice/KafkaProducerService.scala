package zeab.kafkaproducerservice

//Imports
import java.util.UUID
import akka.Done
import akka.actor.{ActorRef, ActorSystem}
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.{ActorMaterializer, OverflowStrategy}
import akka.stream.scaladsl.{Sink, Source}
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.StringSerializer
import zeab.scalaextras.logging.Logging
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

object KafkaProducerService extends App with Logging{

  val kafkaAddress: String = getEnvVar[String]("KAFKA_ADDRESS", "localhost:9092")
  val kafkaTopic: String = getEnvVar[String]("KAFKA_TOPIC", "my-topic")

  //Akka
  implicit val system: ActorSystem = ActorSystem("StandardService", ConfigFactory.load())
  implicit val mat: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher

  //Kafka Producer
  val kafkaProducerConfig: Config = system.settings.config.getConfig("akka.kafka.producer")
  val kafkaProducerSettings: ProducerSettings[String, String] =
    ProducerSettings(kafkaProducerConfig, new StringSerializer, new StringSerializer)
      .withBootstrapServers(kafkaAddress)

  //TODO Change this to a backpressured ack actor
  val kafkaProducerSource: Source[ProducerRecord[String, String], ActorRef] =
    Source.actorRef[ProducerRecord[String, String]](10, OverflowStrategy.dropHead)

  val kafkaProducerSink: Sink[ProducerRecord[String, String], Future[Done]] =
    Producer.plainSink(kafkaProducerSettings)

  val kafkaProducer: ActorRef =
    kafkaProducerSource.toMat(kafkaProducerSink)((actorRef, _) => actorRef).run()

  system.scheduler.schedule(0.second, 1.second){
    kafkaProducer ! new ProducerRecord[String, String](kafkaTopic, s"Ahoy - ${UUID.randomUUID()}")
  }

}
