import akka.actor.ActorSystem
import clients.{MetadataHttpClient, MetadataServiceClient}
import console.{ReadLine, WriteLine}
import errors.ValidationErrorCode
import service.ThumbnailService
import utils.input.InputParser.{getDurationFromStr, validatePath}

import java.io.File
import scala.concurrent.duration.Duration
import scala.sys.exit


object ThumbnailCreatorApp extends App {
   implicit val metadataClient: MetadataHttpClient.Service = new MetadataServiceClient()
   implicit val system = ActorSystem()

  val duration = ReadLine("Enter the point in the video from which to capture the thumbnail:").interpret()
   val pathToDownloadedVideo = ReadLine("Enter the path to the downloaded video:").interpret()

   val durationResult: Either[ValidationErrorCode, Duration] = getDurationFromStr(duration)
   val actualPath: Either[ValidationErrorCode, File] = validatePath(pathToDownloadedVideo)
   val service = new ThumbnailService()
   (durationResult, actualPath) match {
     case (Right(duration), Right(actualPath)) =>
       service.createThumbnail(actualPath, duration)
     case (_, Left(err)) =>
       WriteLine("Provided input (duration and/or path) is invalid")
       exit(0)
     case (Left(err1), Left(err2)) =>
       WriteLine("Provided duration value is invalid. Format should be HH:mm:ss").interpret()
       exit(0)
   }
}


