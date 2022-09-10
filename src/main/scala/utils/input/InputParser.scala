package utils.input

import errors.{InvalidDurationFormat, PathNotFound, ValidationErrorCode}

import java.io.File
import java.time.LocalTime
import scala.concurrent.duration.Duration

object InputParser {

  def getDurationFromStr(durationStr: String): Either[ValidationErrorCode, Duration] = {
    try {
      import java.time.format.DateTimeFormatter
      val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
      val toNanos = LocalTime.parse(durationStr, formatter).toNanoOfDay
      Right(Duration.fromNanos(toNanos))
    } catch {
      case e: Exception => Left(InvalidDurationFormat)
    }
  }

  def validatePath(path: String): Either[ValidationErrorCode, File] = {
    try {
      val file = new File(path)
      file.exists() match {
        case true => Right(file)
        case false => Left(PathNotFound)
      }
    } catch {
      case e: Exception => Left(PathNotFound)
    }
  }

}
