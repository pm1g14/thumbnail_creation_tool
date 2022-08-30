//package clients
//
//import models.{Identifiers, Metadata, VideoQuality}
//import service.TestUtils
//import utils.json.JsonUtils
//
//import scala.concurrent.Future
//
//class MetadataHttpClientMock extends TestUtils {
//  import system.dispatcher
//  def getMetadataForAssetSuccess(assetId: String): Future[Metadata] = Future {
//    val identifiers = Identifiers("abcd", "My Pretty Cat", Some(500), Some(22), "00:00:01")
//    val videoQuality = VideoQuality("ijaw", "high", "aoijwoidj")
//    Metadata(
//      "12345",
//      "12345",
//      "123",
//      "abvc",
//      videoQuality,
//      identifiers
//    )
//  }
//
//  def getMetadataForAssetFailure()
//}
