//Imports
import sbt._

object Dependencies {

  //List of Versions
  val V = new {
    val akka                        = "2.5.22"
    val akkaHttp                    = "10.1.8"
    val akkaHttpCirce               = "1.25.2"
    val akkaKafka                   = "1.0.1"
    val circe                       = "0.11.1"
    val logbackJson                 = "5.2"
    val logback                     = "1.2.3"
    val scalaTest                   = "3.0.5"
    val scalaXML                    = "1.0.6"
    val datastax                    = "3.4.0"
    val zooKeeper                   = "3.4.14"
    val aenea                       = "1.0.0"
    val scalaExtras                 = "1.1.0"
  }

  //List of Dependencies
  val D = new {
    //Akka
    val akkaStream                  = "com.typesafe.akka" %% "akka-stream" % V.akka
    //Akka Http
    val akkaHttp                    = "com.typesafe.akka" %% "akka-http" % V.akkaHttp
    //Akka Kafka
    val akkaKafka                   = "com.typesafe.akka" %% "akka-stream-kafka" % V.akkaKafka
    //Json
    val circeCore                   = "io.circe" %% "circe-parser" % V.circe
    val circeParser                 = "io.circe" %% "circe-generic" % V.circe
    val akkaHttpCirce               = "de.heikoseeberger" %% "akka-http-circe" % V.akkaHttpCirce
    //Logging
    val akkaSlf4j                   = "com.typesafe.akka" %% "akka-slf4j" % V.akka
    //Test
    val scalaTest                   = "org.scalatest" %% "scalatest" % V.scalaTest % "test"
    val akkaTestKit                 = "com.typesafe.akka" %% "akka-testkit" % V.akka % Test
    //Scala XML
    val scalaXML                    = "org.scala-lang.modules" %% "scala-xml" % V.scalaXML
    //Cassandra
    val datastax                    = "com.datastax.cassandra" % "cassandra-driver-core" % V.datastax
    //ZooKeeper
    val zooKeeper                   = "org.apache.zookeeper" % "zookeeper" % V.zooKeeper
    //Xml
    val aenea                       = "com.github.zeab" %% "aenea" % V.aenea
    //ScalaExtras
    val scalaExtras                 = "com.github.zeab" %% "scalaextras" % V.scalaExtras
  }


  val basicServiceDependencies: Seq[ModuleID] = Seq(
    D.akkaStream,
    D.akkaHttp,
    D.akkaSlf4j,
    D.scalaExtras
  )

  val commonDependencies: Seq[ModuleID] = Seq(
    D.akkaStream,
    D.akkaHttp,
    D.akkaHttpCirce,
    D.akkaKafka,
    D.circeCore,
    D.circeParser,
    D.akkaSlf4j,
    D.scalaTest,
    D.akkaTestKit,
    D.scalaXML,
    D.aenea,
    D.datastax,
    D.scalaExtras
  )

}
