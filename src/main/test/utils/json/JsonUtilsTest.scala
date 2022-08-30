package utils.json

import models.Identifiers
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import spray.json.{JsArray, JsNull, JsNumber, JsString, JsValue}

class JsonUtilsTest  extends AnyFlatSpec with Matchers with JsonUtils {
//  "identifiers": {
//    "productionId": "1/1111/1234#001",
//    "title": "My Pretty Cat",
//    "seriesNumber": null,
//    "episodeNumber": null,
//    "duration": "00:00:09"
//  }
  "IdentifiersResponseFormat write" should "return expected value" in {
    val identifier = Identifiers("1/1111/1234#001", "My Pretty Cat", None, None, "00:00:09")
    IdentifiersResponseFormat.write(identifier) shouldBe (JsArray(Vector(JsString("00:00:09"), JsString("My Pretty Cat"), JsString("1/1111/1234#001"))))
  }

  "IdentifiersResponseFormat write" should "return expected value including seriesNumber and episodeNumber" in {
    val identifier = Identifiers("1/1111/1234#001", "My Pretty Cat", Some(10), Some(50), "00:00:09")
    IdentifiersResponseFormat.write(identifier) shouldBe (JsArray(Vector(JsString("00:00:09"), JsString("My Pretty Cat"), JsString("1/1111/1234#001"), JsNumber(50), JsNumber(10))))
  }

//  "IdentifiersResponseFormat read" should "return expected value" in {
//    val jsonValue = JsValue()
//    IdentifiersResponseFormat.read(jsonValue) shouldBe Identifiers("1/1111/1234#001", "My Pretty Cat", Some(10), Some(50), "00:00:09")
  //}

}
