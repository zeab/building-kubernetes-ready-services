package zeab.basichttpservice

//Imports
import zeab.basichttpservice.httpservice.Routes
import zeab.scalaextras.logging.Logging
//Akka
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import com.typesafe.config.ConfigFactory
//Scala
import scala.concurrent.{ExecutionContext, Future}

object BasicHttpService extends App with Logging {

  //Akka
  implicit val system: ActorSystem = ActorSystem("BasicHttpService", ConfigFactory.load())
  implicit val mat: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = system.dispatcher

  //Http Server
  val httpServiceHost: String = getEnvVar[String]("HTTP_SERVICE_HOST", "0.0.0.0")
  val httpServicePort: Int = getEnvVar[Int]("HTTP_SERVICE_PORT", 8080)
  val httpServerSource: Source[Http.IncomingConnection, Future[Http.ServerBinding]] =
    Http().bind(interface = httpServiceHost, httpServicePort)
  implicit val httpBinding: Future[Http.ServerBinding] =
    httpServerSource.to(Sink.foreach { connection =>
      log.debug("Accepted new connection from {}", connection.remoteAddress)
      connection.handleWith(Routes.ingressRoute)
    }).run()
  log.info(s"Http Server is now online at http://$httpServiceHost:$httpServicePort")

}
