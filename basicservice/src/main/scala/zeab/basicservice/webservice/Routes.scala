package zeab.basicservice.webservice

//Imports
import java.util.UUID
//Akka
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

object Routes {

  //Routes dealing with basic ingress checks
  def ingressRoute: Route = {
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
  }

}
