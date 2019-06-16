
//Imports
import Common.seqBaseProjectTemplate
import Versions._
import sbt.Def

//Settings
object Settings {
  
  //Web Services
  val basicServiceSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(basicServiceVersion)
  val standardServiceSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(standardServiceVersion)

  //Kafka
  val kafkaProducerServiceSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(kafkaProducerServiceVersion)
  val kafkaConsumerServiceSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(kafkaConsumerServiceVersion)

  //Cassandra
  val cassandraServiceSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(basicServiceVersion)

  //Putting it all together into a single module
  val k8ReadyServiceSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(basicServiceVersion)
  
}
