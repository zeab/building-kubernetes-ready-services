package zeab.standardhttpservice.httpservice

//Imports
import zeab.akkahttptools.directives.DirectiveExtensions
import zeab.akkahttptools.serialization.{Marshallers, Unmarshallers}
import zeab.standardhttpservice.httpservice.models.{PostPerson200ResponseBody, PostPersonRequestBody}
//Akka
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
//Java
import java.util.UUID
//Circe
import io.circe.generic.auto._

object Routes extends DirectiveExtensions with Marshallers with Unmarshallers {

  //TODO Add more examples

  def all: Route = logRoute{ ingressRoute ~ otherRoute }

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

  def otherRoute: Route =
    pathPrefix("other"){
      post{
        decodeRequest {
          entity(as[PostPersonRequestBody]) { req =>
            complete(OK, PostPerson200ResponseBody(req.name, req.age, UUID.randomUUID.toString))
          }
        }
      }
    }

}
