package zeab.k8readyservice

import zeab.aenea.XmlSerialize
import zeab.k8readyservice.other.{LoyaltyMember, RewardLevel}

object Mine extends App {


  val x = LoyaltyMember(List(RewardLevel(), RewardLevel()))

  val y = XmlSerialize.xmlSerialize(x)

  println(y)

}
