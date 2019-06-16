
//Imports
import Settings._
import Dependencies._
import Docker._
import ModuleNames._
import ModuleNames.{kafkaProducerServiceKey, kafkaConsumerServiceKey, k8ReadyServiceKey}

//Sbt Log Level
logLevel := Level.Info

//Add all the command alias's
CommandAlias.allCommandAlias

lazy val basicservice = (project in file(basicServiceKey))
  .settings(basicServiceSettings: _*)
  .settings(libraryDependencies ++= basicServiceDependencies)
  .settings(basicServiceDockerSettings)
  .enablePlugins(AshScriptPlugin)
  .enablePlugins(AssemblyPlugin)

lazy val standardservice = (project in file(standardServiceKey))
  .settings(standardServiceSettings: _*)
  .settings(libraryDependencies ++= commonDependencies)
  .settings(standardServiceDockerSettings)
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
