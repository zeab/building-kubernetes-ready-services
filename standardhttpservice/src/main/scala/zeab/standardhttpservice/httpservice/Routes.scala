package zeab.standardhttpservice.httpservice

//Imports
import zeab.akkahttptools.directives.DirectiveExtensions
import zeab.akkahttptools.serialization.{Marshallers, Unmarshallers}
import zeab.standardhttpservice.httpservice.models.{GetPerson200ResponseBody, PostPerson200ResponseBody, PostPersonRequestBody}
//Akka
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
//Java
import java.util.UUID
//Circe
import io.circe.generic.AutoDerivation

object Routes extends DirectiveExtensions with Marshallers with Unmarshallers with AutoDerivation {

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
      get{
        complete(OK, GetPerson200ResponseBody("bert", 8, UUID.randomUUID.toString))
      } ~
      post{
        decodeRequest {
          entity(as[PostPersonRequestBody]) { req =>
            complete(OK, PostPerson200ResponseBody(req.name, req.age, UUID.randomUUID.toString))
          }
        }
      }
    }

}
