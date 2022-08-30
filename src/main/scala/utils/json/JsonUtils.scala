package utils.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import models.{Identifiers, Metadata, VideoQuality}
import spray.json.DefaultJsonProtocol.{StringJsonFormat, jsonFormat3, jsonFormat6}
import spray.json.{JsArray, JsNull, JsNumber, JsString, JsValue, RootJsonFormat, deserializationError}

trait JsonUtils extends SprayJsonSupport {
  implicit val VideoQualityResponseFormat: RootJsonFormat[VideoQuality] = jsonFormat3(VideoQuality)
  implicit object IdentifiersResponseFormat extends RootJsonFormat[Identifiers] {
    def write(c: Identifiers) = {
      (c.episodeNumber, c.seriesNumber) match {
        case (Some(episodeNum), Some(seriesNum)) =>
          JsArray(JsString(c.duration), JsString(c.title), JsString(c.productionId), JsNumber(episodeNum), JsNumber(seriesNum))
        case (None, None) => JsArray(JsString(c.duration), JsString(c.title), JsString(c.productionId))
        case (Some(episodeNum), None) => JsArray(JsString(c.duration), JsString(c.title), JsString(c.productionId), JsNumber(episodeNum))
        case (None, Some(seriesNum)) => JsArray(JsString(c.duration), JsString(c.title), JsString(c.productionId), JsNumber(seriesNum))
      }
    }

    def read(value: JsValue) = {
      val jso = value.asJsObject
      val duration = jso.fields.get("duration").asInstanceOf[Option[JsString]]
      val episodeNum = parseJsValue(jso.fields.get("episodeNumber"))
      val seriesNum = parseJsValue(jso.fields.get("seriesNumber"))
      val productionId = jso.fields.get("productionId").asInstanceOf[Option[JsString]]
      val title = jso.fields.get("title").asInstanceOf[Option[JsString]]
      Identifiers(
        productionId.map(_.value).getOrElse(""),
        title.map(_.value).getOrElse(""),
        seriesNum,
        episodeNum,
        duration.map(_.value).getOrElse("")
      )
    }
  }
  implicit val MetadataResponseFormat: RootJsonFormat[Metadata] = jsonFormat6(Metadata)
  private def parseJsValue(x: Option[JsValue]): Option[Int] ={
    x.flatMap {
      _ match {
        case JsString(value) => Some(Integer.parseInt(value))
        case JsNumber(value) => Some(value.toInt)
        case JsNull => None
        case _ => deserializationError("unknown value for JsObject")
      }
    }
  }
}
