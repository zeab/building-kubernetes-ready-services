package zeab.udpclient

//Imports
import zeab.scalaextras.logging.Logging
//Java
import java.util.UUID
import java.net.InetSocketAddress
//Akka
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.{Logging, LoggingAdapter}
import akka.io.{IO, UdpConnected}
import akka.util.ByteString
import com.typesafe.config.ConfigFactory
//Scala
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext

object UdpClient extends App with Logging {

  val udpHost: String = getEnvVar[String]("UDP_HOST", "localhost")
  val udpPort: String = getEnvVar[String]("UDP_PORT", "8125")

  //Akka
  implicit val system: ActorSystem = ActorSystem("UdpClient", ConfigFactory.load())
  implicit val ec: ExecutionContext = system.dispatcher

  val udpClient: ActorRef = system.actorOf(Props(classOf[UdpClient], udpHost, udpPort))

  system.scheduler.schedule(0.second, 1.second) {
    udpClient ! SendUdpDatagram(s"Ahoy! ${UUID.randomUUID()}")
  }

}

class UdpClient(host: String, port: String) extends Actor {

  val actorLog: LoggingAdapter = Logging(context.system, this)

  val remote: InetSocketAddress = new InetSocketAddress(host, port.toInt)

  implicit val actorSystem: ActorSystem = context.system

  def receive: Receive = {
    case UdpConnected.Connected =>
      actorLog.debug(s"Connecting udp $host:$port")
      context.become(ready(sender()))
  }

  def ready(connection: ActorRef): Receive = {
    case UdpConnected.Received(data) =>
      actorLog.debug(s"received msg: $data")
    //What to do if I get a message from the connected service
    case msg: SendUdpDatagram =>
      actorLog.debug(s"Sending Udp $host:$port msg: ${msg.datagram}")
      connection ! UdpConnected.Send(ByteString(msg.datagram))
    case UdpConnected.Disconnect =>
      actorLog.debug(s"disconnecting udp $host:$port")
      connection ! UdpConnected.Disconnect
    case UdpConnected.Disconnected =>
      actorLog.debug(s"disconnected udp $host:$port")
      context.stop(self)
  }

  override def preStart: Unit = {
    IO(UdpConnected) ! UdpConnected.Connect(self, remote)
  }

}
