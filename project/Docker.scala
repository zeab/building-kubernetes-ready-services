
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

  val udpServiceDockerSettings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := I.openjdk8Alpine,
    dockerRepository := repo,
    dockerLabels := mapDockerLabels(udpServiceKey, udpServiceVersion, buildTime),
    dockerUpdateLatest := true
  )

  val udpClientDockerSettings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := I.openjdk8Alpine,
    dockerRepository := repo,
    dockerLabels := mapDockerLabels(udpClientKey, udpClientVersion, buildTime),
    dockerUpdateLatest := true
  )

  val basicHttpServiceDockerSettings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := I.openjdk8Alpine,
    dockerRepository := repo,
    dockerLabels := mapDockerLabels(basicHttpServiceKey, basicHttpServiceVersion, buildTime),
    dockerUpdateLatest := true
  )

  val standardHttpServiceDockerSettings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := I.openjdk8Alpine,
    dockerRepository := repo,
    dockerLabels := mapDockerLabels(standardHttpServiceKey, standardHttpServiceVersion, buildTime),
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

  val cassandraServiceDockerSettings: Seq[Def.Setting[_]] = Seq(
    dockerBaseImage := I.openjdk8Alpine,
    dockerRepository := repo,
    dockerLabels := mapDockerLabels(cassandraServiceKey, cassandraServiceVersion, buildTime),
    dockerUpdateLatest := true
  )

}
