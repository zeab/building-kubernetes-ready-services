
//Imports
import Common.seqBaseProjectTemplate
import Versions._
import sbt.Def

//Settings
object Settings {

  //Udp Services
  val udpServiceSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(udpServiceVersion)
  val udpClientSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(udpClientVersion)
  
  //Http Services
  val basicHttpServiceSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(basicHttpServiceVersion)
  val standardHttpServiceSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(standardHttpServiceVersion)

  //Kafka
  val kafkaProducerServiceSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(kafkaProducerServiceVersion)
  val kafkaConsumerServiceSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(kafkaConsumerServiceVersion)

  //Cassandra
  val cassandraServiceSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(cassandraServiceVersion)

  //Putting it all together into a single module
  val k8ReadyServiceSettings: Seq[Def.Setting[_]] = seqBaseProjectTemplate(k8ReadyServiceVersion)
  
}
