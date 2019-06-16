package zeab.k8readyservice.httpservice

//Imports
import zeab.akkahttptools.directives.DirectiveExtensions
import zeab.akkahttptools.serialization.{Marshallers, Unmarshallers}
import zeab.k8readyservice.httpservice.models.HttpError
//Datastax
import com.datastax.driver.core.Session
//Circe
import io.circe.generic.AutoDerivation
//ZooKeeper
import org.apache.zookeeper.ZooKeeper
//Scala
import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}
//Akka
import akka.event.LoggingAdapter
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
//Java
import java.util.UUID

object Routes extends DirectiveExtensions with Marshallers with Unmarshallers with AutoDerivation {

  //TODO Add more examples

  def all(implicit zookeeper: ZooKeeper, session: Session): Route =
    logRoute {
      ingressRoute ~ checkRoute("liveness") ~ checkRoute("readiness")
    }

  //Routes dealing with basic ingress checks
  def ingressRoute: Route =
    pathPrefix("ingress") {
      get {
        complete(OK, s"Get Ingress - ${UUID.randomUUID}")
      } ~
        post {
          complete(Created, s"Post Ingress - ${UUID.randomUUID}")
        } ~
        put {
          complete(Accepted, s"Put Ingress - ${UUID.randomUUID}")
        } ~
        delete {
          complete(OK, s"Delete Ingress - ${UUID.randomUUID}")
        }
    }


  //Used for the readiness and liveness checks
  def checkRoute(path: String)(implicit zooKeeper: ZooKeeper, session: Session): Route =
    extractExecutionContext { implicit ec: ExecutionContext =>
      extractLog { log: LoggingAdapter =>
        pathPrefix(path) {
          get {
            val isCassandraConnected: Future[Boolean] =
              Future {
                if (session.getState.getConnectedHosts.asScala.toList.nonEmpty) true
                else false
              }
            val isZooKeeperConnected: Future[Boolean] =
              Future {
                zooKeeper.getState.toString match {
                  case "CONNECTED" => true
                  case _ => false
                }
              }
            val isKafkaConnected: Future[Boolean] =
              Future {
                val brokers: List[String] =
                  Try(zooKeeper.getChildren("/brokers/ids", false).asScala.toList) match {
                    case Success(value) => value
                    case Failure(_) => List.empty
                  }
                if (brokers.isEmpty) false
                else true
              }
            val check: Future[Boolean] =
              for {
                cas <- isCassandraConnected
                zoo <- isZooKeeperConnected
                kaf <- isKafkaConnected
              } yield {
                val checkResults: Boolean = cas & kaf & zoo
                if (true) log.info(s"$path check results | cas: $cas | zoo: $zoo | kaf: $kaf")
                else log.error(s"$path check results | cas: $cas | zoo: $zoo | kaf: $kaf")
                checkResults
              }
            onComplete(check) {
              case Success(checkResult) =>
                if (checkResult) complete(OK, s"$path check passed")
                else complete(InternalServerError, HttpError(1, s"$path check failed"))
              case Failure(_) => complete(InternalServerError, HttpError(1, s"$path check failed"))
            }
          }
        }
      }
    }

}
