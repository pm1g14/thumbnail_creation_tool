package utils.input

import errors.{InvalidDurationFormat, PathNotFound}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import java.io.File
import scala.concurrent.duration.{Duration, HOURS, MINUTES, SECONDS}

class InputParserTest  extends AnyFlatSpec with Matchers{

  "getDurationFromStr" should "return InvalidDurationFormat for wrong formatting" in {
    InputParser.getDurationFromStr("0:00") shouldBe Left(InvalidDurationFormat)
    InputParser.getDurationFromStr("0.00") shouldBe Left(InvalidDurationFormat)
  }

  "getDurationFromStr" should "return correct result for correct duration formatting" in {
    InputParser.getDurationFromStr("00:00:01") shouldBe Right(Duration(1, SECONDS))
    InputParser.getDurationFromStr("00:10:00") shouldBe Right(Duration(10, MINUTES))
    InputParser.getDurationFromStr("10:00:00") shouldBe Right(Duration(10, HOURS))
    InputParser.getDurationFromStr("01:00:00") shouldBe Right(Duration(1, HOURS))
  }

  "validatePath" should "return new file for valid path" in {
    InputParser.validatePath("C:\\development\\cst-eng-pm1g14\\resources\\video\\samples\\valid.mov") shouldBe
      Right(new File("C:\\development\\cst-eng-pm1g14\\resources\\video\\samples\\valid.mov"))
  }

  "validatePath" should "return PathNotFound for invalid path" in {
    InputParser.validatePath("C:\\cst-eng-pm1g14\\resources\\video\\samples\\valid.mov") shouldBe
      Left(PathNotFound)
  }

}
