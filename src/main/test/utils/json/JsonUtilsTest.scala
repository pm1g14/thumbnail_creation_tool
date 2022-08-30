package utils.json

import models.Identifiers
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import spray.json.{JsArray, JsNull, JsNumber, JsString, JsValue}

class JsonUtilsTest  extends AnyFlatSpec with Matchers with JsonUtils {

  "IdentifiersResponseFormat write" should "return expected value" in {
    val identifier = Identifiers("1/1111/1234#001", "My Pretty Cat", None, None, "00:00:09")
    IdentifiersResponseFormat.write(identifier) shouldBe (JsArray(Vector(JsString("00:00:09"), JsString("My Pretty Cat"), JsString("1/1111/1234#001"))))
  }

  "IdentifiersResponseFormat write" should "return expected value including seriesNumber and episodeNumber" in {
    val identifier = Identifiers("1/1111/1234#001", "My Pretty Cat", Some(10), Some(50), "00:00:09")
    IdentifiersResponseFormat.write(identifier) shouldBe (JsArray(Vector(JsString("00:00:09"), JsString("My Pretty Cat"), JsString("1/1111/1234#001"), JsNumber(50), JsNumber(10))))
  }

}
