package utils.checksum

import java.io.{File, FileInputStream}
import java.security.{DigestInputStream, MessageDigest}
import scala.util.Try

object ChecksumUtils {

  val calculateChecksumForFile: (File, ChecksumAlgorithm) => Option[String] = (file: File, algorithm: ChecksumAlgorithm) => {
    Try {
      val digest = MessageDigest.getInstance(algorithm.toString)
      val dataBytes = new Array[Byte](8192)
      val dis = new DigestInputStream(new FileInputStream(file), digest)
      while (dis.read(dataBytes) != -1) {}
      dis.close()
      digest.digest.map("%02x".format(_)).mkString
    }.toOption
  }

  val compare: (String, String) => Boolean = (checksum1: String, checksum2: String) => checksum1.equals(checksum2)

}

sealed trait ChecksumAlgorithm {
  def toString: String
}

case object SHA256 extends ChecksumAlgorithm {
  override def toString: String = "SHA256"
}

case object SHA1 extends ChecksumAlgorithm {
  override def toString: String = "SHA1"
}

case object MD5 extends ChecksumAlgorithm {
  override def toString: String = "MD5"
}