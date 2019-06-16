
//Imports
import Common.buildTime

//Versions for all the modules
object Versions {

  //Web Services
  val basicServiceVersion: String = s"0.0.$buildTime"
  val standardServiceVersion: String = s"0.0.$buildTime"

  //Kafka
  val kafkaProducerServiceVersion: String = s"0.0.$buildTime"
  val kafkaConsumerServiceVersion: String = s"0.0.$buildTime"

  //Cassandra
  val cassandraServiceVersion: String = s"0.0.$buildTime"

  //Putting it all together into a single module
  val k8ReadyServiceVersion: String = s"0.0.$buildTime"

}
