package console

sealed trait Console[A] {
  def interpret(): A
}

case class Execute(cmd: String) extends Console[String] {
  import scala.sys.process._

  def interpret(): String = {
    cmd .!!
  }
}
case class ReadLine(message: String) extends Console[String] {
  def interpret(): String = {
    scala.io.StdIn.readLine(message)
  }
}

case class WriteLine(message: String) extends Console[String] {
  override def interpret(): String = {
    println(message)
    message
  }
}
