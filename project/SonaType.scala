
//Imports
import sbt.Keys._
import sbt._
import sbt.librarymanagement.ivy.Credentials

object SonaType extends AutoPlugin{

  def getSonaTypeCredentials: Seq[Def.Setting[_]] = {

    val userName: Option[String] = sys.env.get("SONATYPE_USERNAME")
    val password: Option[String] = sys.env.get("SONATYPE_PASSWORD")

    (userName, password) match {
      case (Some(un), Some(pw)) =>
        credentials += Credentials(
          "Sonatype Nexus Repository Manager",
          "oss.sonatype.org",
          un,
          pw
        )
      case (Some(_), None) => Seq.empty
      case (None, Some(_)) => Seq.empty
      case (None, None) => Seq.empty
    }

  }

}
