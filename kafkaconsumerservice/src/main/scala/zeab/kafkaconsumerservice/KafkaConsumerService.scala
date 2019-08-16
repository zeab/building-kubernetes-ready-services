package zeab.kafkaconsumerservice

import java.util.UUID

import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.scaladsl.Consumer.Control
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}
import zeab.scalaextras.logging.Logging

import scala.concurrent.ExecutionContext
import scala.io.Source

object KafkaConsumerService extends App with Logging {

  val kafkaAddress: String = getEnvVar[String]("KAFKA_ADDRESS", "localhost:9092")
  val kafkaTopic: String = getEnvVar[String]("KAFKA_TOPIC", "my-topic")

  //Akka
  implicit val system: ActorSystem = ActorSystem("StandardService", ConfigFactory.load())
  implicit val mat: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher

  //Kafka Consumer
  //So that when other messages are trapped by other services we will also consume them and make sure the all data is accessible to everyone

  //Kafka Consumer Settings
  val kafkaConsumerConfig: Config = system.settings.config.getConfig("akka.kafka.consumer")

  val kafkaConsumerSettings: ConsumerSettings[String, Array[Byte]] =
    ConsumerSettings(kafkaConsumerConfig, new StringDeserializer, new ByteArrayDeserializer)
      .withBootstrapServers(kafkaAddress)
      .withGroupId(UUID.randomUUID().toString)
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  val kafkaConsumerSource =
    Consumer.plainSource(kafkaConsumerSettings, Subscriptions.topics(kafkaTopic))

  kafkaConsumerSource
    .runForeach { record =>
      println(record.value().map(_.toChar).mkString)
      system.eventStream.publish(record)
    }

}
