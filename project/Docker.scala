
//Imports
import Common._
import Versions._
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport._
import sbt.Def
import ModuleNames._

object Docker {

  val repo: Option[String] = Some("zeab")

  //Image List
  val I = new {
    val openjdk8Alpine: String = "openjdk:8-jdk-alpine"
    val openjdk8Slim: String = "openjdk:8-jdk-slim"
  }

  //Kafka
  val kafkaProducerServiceKey: String = "kafkaproducerservice"
  val kafkaConsumerServiceKey: String = "kafkaconsumerservice"

  //Cassandra
  val cassandraServiceKey: String = "cassandraservice"

  //Putting it all together into a single module
  val k8ReadyServiceKey: String = "k8readyservice"
  
  val basicServiceDockerSettings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := I.openjdk8Alpine,
    dockerRepository := repo,
    dockerLabels := mapDockerLabels(basicServiceKey, basicServiceVersion, buildTime),
    dockerUpdateLatest := true
  )

  val standardServiceDockerSettings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := I.openjdk8Alpine,
    dockerRepository := repo,
    dockerLabels := mapDockerLabels(standardServiceKey, standardServiceVersion, buildTime),
    dockerUpdateLatest := true
  )

  val kafkaProducerServiceDockerSettings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := I.openjdk8Alpine,
    dockerRepository := repo,
    dockerLabels := mapDockerLabels(kafkaProducerServiceKey, kafkaProducerServiceVersion, buildTime),
    dockerUpdateLatest := true
  )

  val kafkaConsumerServiceDockerSettings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := I.openjdk8Alpine,
    dockerRepository := repo,
    dockerLabels := mapDockerLabels(kafkaConsumerServiceKey, kafkaConsumerServiceVersion, buildTime),
    dockerUpdateLatest := true
  )

  val k8ReadyServiceDockerSettings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := I.openjdk8Alpine,
    dockerRepository := repo,
    dockerLabels := mapDockerLabels(k8ReadyServiceKey, k8ReadyServiceVersion, buildTime),
    dockerUpdateLatest := true
  )
  
}
