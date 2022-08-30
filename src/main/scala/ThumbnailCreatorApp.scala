import akka.actor.ActorSystem
import clients.{MetadataHttpClient, MetadataServiceClient}
import console.{Console, Execute, ReadLine, WriteLine}
import errors.ValidationErrorCode
import service.ThumbnailService
import utils.input.InputParser
import utils.input.InputParser.getDurationFromStr

import java.io.File
import java.time.LocalTime
import scala.concurrent.duration.Duration
import scala.sys.exit


object ThumbnailCreatorApp extends App {

   implicit val metadataClient: MetadataHttpClient.Service = new MetadataServiceClient()
   implicit val system = ActorSystem()

   val duration = ReadLine("Enter the point in the video from which to capture the thumbnail:").interpret()
   val pathToDownloadedVideo = ReadLine("Enter the path to the downloaded video:").interpret()

   val durationResult: Either[ValidationErrorCode, Duration] = getDurationFromStr(duration)
   val actualPath = new File(pathToDownloadedVideo)
   val service = new ThumbnailService()
   (durationResult) match {
     case Right(duration) =>
       service.createThumbnail(actualPath, duration)
     case Left(err) =>
       WriteLine("Provided duration value is invalid. Format should be HH:mm:ss").interpret()
       exit(0)
   }
}


