
//Imports
import Settings._
import Dependencies._
import Docker._
import ModuleNames._

//Sbt Log Level
logLevel := Level.Info

//Add all the command alias's
CommandAlias.allCommandAlias

lazy val basichttpservice = (project in file(basicHttpServiceKey))
  .settings(basicHttpServiceSettings: _*)
  .settings(libraryDependencies ++= standardServiceDependencies)
  .settings(basicHttpServiceDockerSettings)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(AssemblyPlugin)

lazy val standardhttpservice = (project in file(standardHttpServiceKey))
  .settings(standardHttpServiceSettings: _*)
  .settings(libraryDependencies ++= standardServiceDependencies)
  .settings(standardHttpServiceDockerSettings)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(AssemblyPlugin)

lazy val udpservice = (project in file(udpServiceKey))
  .settings(udpServiceSettings: _*)
  .settings(libraryDependencies ++= standardServiceDependencies)
  .settings(udpServiceDockerSettings)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(AssemblyPlugin)

lazy val udpclient = (project in file(udpClientKey))
  .settings(udpClientSettings: _*)
  .settings(libraryDependencies ++= standardServiceDependencies)
  .settings(udpClientDockerSettings)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(AssemblyPlugin)

lazy val kafkaproducerservice = (project in file(kafkaProducerServiceKey))
  .settings(kafkaProducerServiceSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .settings(kafkaProducerServiceDockerSettings)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(AssemblyPlugin)

lazy val kafkaconsumerservice = (project in file(kafkaConsumerServiceKey))
  .settings(kafkaConsumerServiceSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .settings(kafkaConsumerServiceDockerSettings)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(AssemblyPlugin)

lazy val k8readyservice = (project in file(k8ReadyServiceKey))
  .settings(kafkaConsumerServiceSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .settings(kafkaConsumerServiceDockerSettings)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(AssemblyPlugin)

lazy val cassandraservice = (project in file(cassandraServiceKey))
  .settings(cassandraServiceSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .settings(cassandraServiceDockerSettings)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(AssemblyPlugin)