package zeab.udpclient

//Imports

import java.util.UUID

import akka.actor.Props
import com.typesafe.config.ConfigFactory
import zeab.scalaextras.logging.Logging

import scala.concurrent.ExecutionContext
//Java
import java.net.InetSocketAddress
//Akka
import akka.actor.{Actor, ActorRef, ActorSystem}
import akka.event.{Logging, LoggingAdapter}
import akka.io.{IO, UdpConnected}
import akka.util.ByteString

import scala.concurrent.duration._

object UdpClient extends App with Logging {

  val host: String = "192.168.1.1"
  val port: String = "8125"

  //Akka
  implicit val system: ActorSystem = ActorSystem("UdpClient", ConfigFactory.load())
  implicit val ec: ExecutionContext = system.dispatcher

  val udpClient: ActorRef = system.actorOf(Props(classOf[UdpClient], host, port))

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
