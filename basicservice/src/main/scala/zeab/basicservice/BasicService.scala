package zeab.basicservice

//Imports
import zeab.basicservice.webservice.Routes
import zeab.scalaextras.logging.Logging
//Akka
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.typesafe.config.ConfigFactory
//Scala
import scala.concurrent.{ExecutionContext, Future}

object BasicService extends App with Logging {

  //Akka
  implicit val system: ActorSystem = ActorSystem("BasicService", ConfigFactory.load())
  implicit val mat: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher

  //Http Server
  val webServiceHost: String = getEnvVar[String]("WEB_SERVICE_HOST", "0.0.0.0")
  val webServicePort: Int = getEnvVar[Int]("WEB_SERVICE_PORT", 8080)
  val webServerSource: Source[Http.IncomingConnection, Future[Http.ServerBinding]] =
    Http().bind(interface = webServiceHost, webServicePort)
  implicit val binding: Future[Http.ServerBinding] =
    webServerSource.to(Sink.foreach { connection =>
      log.debug("Accepted new connection from {}", connection.remoteAddress)
      connection.handleWith(Routes.ingressRoute)
    }).run()
  log.info(s"Http Server is now online at http://$webServiceHost:$webServicePort")

}
