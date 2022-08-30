package service

import akka.actor.ActorSystem
import clients.{MetadataHttpClient, MetadataServiceClient}
import console.{Execute, WriteLine}
import models.{Identifiers, Metadata, VideoQuality}
import org.mockito.Mockito
import org.mockito.Mockito.{mock, when}
import org.scalatest.BeforeAndAfter
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.File
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.{Duration, SECONDS}
import scala.reflect.io.Directory

class ThumbnailServiceTest  extends AnyFlatSpec with Matchers with TestUtils with BeforeAndAfter {
  private val service = new ThumbnailService()
  import system.dispatcher

  private implicit val mockClient = Mockito.mock(classOf[MetadataServiceClient])
  private val pathToThumbnail = s"${System.getProperty("user.dir")}\\thumbnail.png"

  //delete the thumbnail before each run
  before {
    val dir = new Directory(new File(pathToThumbnail))
    dir.deleteRecursively()
  }

  "createThumbnail" should "create a thumbnail to the given path for valid checksum and duration" in {
    when(mockClient.getMetadataForAsset("valid")).thenReturn(
      Future {
        val identifiers = Identifiers("abcd", "My Pretty Cat", Some(500), Some(22), "00:00:15")
        val videoQuality = VideoQuality("ijaw", "high", "aoijwoidj")
        Metadata(
              "12345",
              "6f314c1d10090f43422756a0451509a8481d87a0a9e3a26ca073a98af5523247",
              "123",
              "abvc",
              videoQuality,
              identifiers
        )
      }
    )
    service.createThumbnail(
      new File("C:\\development\\cst-eng-pm1g14\\resources\\video\\samples\\valid.mov"),
      Duration(10, SECONDS)
    )(mockClient, system)
    Thread.sleep(5000) //by this time the file will be saved
    new File(pathToThumbnail).exists() shouldBe true
  }

  "createThumbnail" should "not create a thumbnail for invalid checksum" in {
    when(mockClient.getMetadataForAsset("valid")).thenReturn(
      Future {
        val identifiers = Identifiers("abcd", "My Pretty Cat", Some(500), Some(22), "00:00:01")
        val videoQuality = VideoQuality("ijaw", "high", "aoijwoidj")
        Metadata(
          "12345",
          "12345",
          "123",
          "abvc",
          videoQuality,
          identifiers
        )
      }
    )
    service.createThumbnail(
      new File("C:\\development\\cst-eng-pm1g14\\resources\\video\\samples\\valid.mov"),
      Duration(10, SECONDS)
    )(mockClient, system)
    Thread.sleep(5000) //by this time the file will be saved
    new File(pathToThumbnail).exists() shouldBe false
  }

  "createThumbnailOrNone" should "return Execute command for valid thumbnail duration" in {
    val file = new File("C:\\development\\cst-eng-pm1g14\\resources\\video\\samples\\valid.mov")
    service.createThumbnailOrNone(
      actualDuration = Duration(10, SECONDS),
      metadataDuration = Duration(11, SECONDS),
      file = file
    ) shouldBe Some(Execute(s"ffmpeg -ss 00:00:5 -i ${file.getPath} -vframes 1 thumbnail.png"))
  }

  "createThumbnailOrNone" should "return None command for thumbnail actual duration greater than metadata duration" in {
    val file = new File("C:\\development\\cst-eng-pm1g14\\resources\\video\\samples\\valid.mov")
    service.createThumbnailOrNone(
      actualDuration = Duration(20, SECONDS),
      metadataDuration = Duration(11, SECONDS),
      file = file
    ) shouldBe None
  }


  "handleThumbnailCreation" should "return successful write message in console for creation of thumbnail" in {
    val file = new File("C:\\development\\cst-eng-pm1g14\\resources\\video\\samples\\valid.mov")

    service.handleThumbnailCreation(
      file = file,
      duration = Duration(10, SECONDS),
      calculatedChecksum = Some("6f314c1d10090f43422756a0451509a8481d87a0a9e3a26ca073a98af5523247"),
      metadataChecksum = "6f314c1d10090f43422756a0451509a8481d87a0a9e3a26ca073a98af5523247",
      metadataDuration = Some(Duration(11, SECONDS))
    ) shouldBe WriteLine(s"Thumbnail created under ${file.getPath}!")
  }

  "handleThumbnailCreation" should "return that it can't create thumbnail because of wrong duration" in {
    val file = new File("C:\\development\\cst-eng-pm1g14\\resources\\video\\samples\\valid.mov")
    service.handleThumbnailCreation(
      file = file,
      duration = Duration(20, SECONDS),
      calculatedChecksum = Some("6f314c1d10090f43422756a0451509a8481d87a0a9e3a26ca073a98af5523247"),
      metadataChecksum = "6f314c1d10090f43422756a0451509a8481d87a0a9e3a26ca073a98af5523247",
      metadataDuration = Some(Duration(11, SECONDS))
    ) shouldBe WriteLine("Could not create thumbnail")
  }

  "handleThumbnailCreation" should "return that it can't create thumbnail because of checksum mismatch" in {
    val file = new File("C:\\development\\cst-eng-pm1g14\\resources\\video\\samples\\valid.mov")
    service.handleThumbnailCreation(
      file = file,
      duration = Duration(10, SECONDS),
      calculatedChecksum = Some("6f314c1d10090f43422756a0451509a8481d87a0a9e3a26ca073a98af5523247"),
      metadataChecksum = "6f314c1d18481d87a0a9e3a26ca073a98af5523247",
      metadataDuration = Some(Duration(11, SECONDS))
    ) shouldBe WriteLine("Cannot create thumbnail, checksums do not match")
  }
}

trait TestUtils {
  implicit val system = ActorSystem()
}
