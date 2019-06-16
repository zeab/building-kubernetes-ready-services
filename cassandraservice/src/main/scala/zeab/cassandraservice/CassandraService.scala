package zeab.cassandraservice

//Imports
import com.datastax.driver.core.Row
import zeab.scalaextras.logging.Logging
//Datastax
import com.datastax.driver.core.{Cluster, Session}
//Scala
import scala.collection.JavaConverters._

object CassandraService extends App with Logging {

  //Cassandra
  implicit val cluster: Cluster =
    Cluster.builder
      .addContactPoint("192.168.1.144")
      //.withPort(getEnvVar[Int]("PORT"))
      //.withCredentials(getEnvVar[String]("USER"), getEnvVar[String]("PASS"))
      .build

  implicit val session: Session = cluster.connect()

  val query: String =
    s"""SELECT * FROM some.table;""".stripMargin

  val rows: List[Row] = session.execute(query).asScala.toList

  rows.foreach(println)

}
