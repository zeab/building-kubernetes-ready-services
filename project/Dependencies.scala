//Imports
import sbt._

object Dependencies {

  //List of Versions
  val V = new {
    val akka                        = "2.5.22"
    val akkaKafka                   = "1.0.1"
    val scalaTest                   = "3.0.5"
    val datastax                    = "3.4.0"
    val zooKeeper                   = "3.4.14"
    val scalaExtras                 = "1.1.0"
    val akkaHttpTools               = "1.0.+"
  }

  //List of Dependencies
  val D = new {
    //Akka
    val akkaStream                  = "com.typesafe.akka" %% "akka-stream" % V.akka
    //Akka Http
    val akkaHttpTools               = "com.github.zeab" %% "akkahttptools" % V.akkaHttpTools
    //Akka Kafka
    val akkaKafka                   = "com.typesafe.akka" %% "akka-stream-kafka" % V.akkaKafka
    //Test
    val scalaTest                   = "org.scalatest" %% "scalatest" % V.scalaTest % "test"
    val akkaTestKit                 = "com.typesafe.akka" %% "akka-testkit" % V.akka % Test
    //Cassandra
    val datastax                    = "com.datastax.cassandra" % "cassandra-driver-core" % V.datastax
    //ZooKeeper
    val zooKeeper                   = "org.apache.zookeeper" % "zookeeper" % V.zooKeeper
    //ScalaExtras
    val scalaExtras                 = "com.github.zeab" %% "scalaextras" % V.scalaExtras
  }

  val standardServiceDependencies: Seq[ModuleID] = Seq(
    D.akkaStream,
    D.akkaHttpTools,
    D.scalaExtras
  )

  val cassandraServiceDependencies: Seq[ModuleID] = Seq(
    D.akkaStream,
    D.scalaExtras,
    D.datastax
  )

  val commonDependencies: Seq[ModuleID] = Seq()

}
