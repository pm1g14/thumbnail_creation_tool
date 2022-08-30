package clients

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Get
import akka.http.scaladsl.unmarshalling.Unmarshal
import models.Metadata
import utils.json.JsonUtils

import scala.concurrent.Future

object MetadataHttpClient {

  trait Service {

    def getMetadataForAsset(assetId: String): Future[Metadata]
  }

}

class MetadataServiceClient extends MetadataHttpClient.Service with JsonUtils {
  implicit val system = ActorSystem()
  import system.dispatcher

  override def getMetadataForAsset(assetId: String): Future[Metadata] = {
    val request = Get(s"https://cdfr062ui5.execute-api.eu-west-1.amazonaws.com/playground/${assetId}/metadata")
    Http().singleRequest(request).flatMap(r => Unmarshal(r.entity).to[Metadata])
  }

}
