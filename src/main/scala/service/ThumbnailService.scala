package service

import akka.actor.ActorSystem
import clients.MetadataHttpClient
import console.{Console, Execute, WriteLine}
import errors.MetadataRetrievalError
import utils.checksum.ChecksumUtils.{calculateChecksumForFile, compare}
import utils.checksum.SHA256

import java.io.File
import java.time.LocalTime
import scala.concurrent.duration.{Duration, FiniteDuration}
import scala.util.{Failure, Success}


class ThumbnailService {
  def createThumbnail(file: File, duration: Duration)(implicit client: MetadataHttpClient.Service, actorSystem: ActorSystem): Unit = {
    import actorSystem.dispatcher

    val calculatedChecksum = calculateChecksumForFile(file, SHA256)
    val response = client.getMetadataForAsset("valid"); //TODO make this configurable from input
    response.onComplete {
      case Failure(_) => MetadataRetrievalError("valid")
      case Success(res) =>
        val metadataDuration = try {
          val toNanos = LocalTime.parse(res.identifiers.duration).toNanoOfDay
          Some(Duration.fromNanos(toNanos))
        } catch {
          case e: Exception => None
        }

        handleThumbnailCreation(file, duration, calculatedChecksum, res.sha256, metadataDuration).interpret()
    }
  }

  private[service] def handleThumbnailCreation(
     file: File,
     duration: Duration,
     calculatedChecksum: Option[String],
     metadataChecksum: String,
     metadataDuration: Option[FiniteDuration]
  ) = {
    if (isValidChecksum(calculatedChecksum, metadataChecksum)) {
      val maybeCreatedThumbnail = metadataDuration match {
        case Some(value) => createThumbnailOrNone(duration, value, file)
        case None => None
      }
      maybeCreatedThumbnail match {
        case Some(execute) =>
          execute.interpret()
          WriteLine(s"Thumbnail created under ${System.getProperty("user.dir")}!")
        case None => WriteLine("Could not create thumbnail")
      }
    } else {
      WriteLine("Cannot create thumbnail, checksums do not match")
    }
  }

  private val isValidChecksum = (calculatedChecksum: Option[String], res: String) => compare(calculatedChecksum.getOrElse(""), res)

  private val checkDuration = (actualDuration: Duration, metadataDuration: Duration) => actualDuration.compareTo(metadataDuration)

  private[service] def createThumbnailOrNone(actualDuration: Duration, metadataDuration: Duration, file: File): Option[Console[String]] = {
    checkDuration(actualDuration, metadataDuration) match {
      case value:Int if value <= 0 => Some(Execute(s"ffmpeg -ss 00:00:5 -i ${file.getPath} -vframes 1 thumbnail.png"))
      case _ => None
    }
  }
}
