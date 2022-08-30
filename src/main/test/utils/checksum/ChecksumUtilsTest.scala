package utils.checksum

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import utils.checksum.ChecksumUtils.calculateChecksumForFile

import java.io.File

class ChecksumUtilsTest extends AnyFlatSpec with Matchers {

    "calculateChecksumForFile" should "return some sha256 checksum for a file that exists" in {
       val result =
         calculateChecksumForFile(new File("C:\\development\\cst-eng-pm1g14\\resources\\video\\samples\\valid.mov"), SHA256)
        result shouldBe Some("6f314c1d10090f43422756a0451509a8481d87a0a9e3a26ca073a98af5523247")
    }

    "calculateChecksumForFile" should "return None for missing file" in {
      val result =
        calculateChecksumForFile(new File(""), SHA256)
      result shouldBe None
    }

  "compare" should "return true for 2 identical sha256 strings" in {
    ChecksumUtils
      .compare("6f314c1d10090f43422756a0451509a8481d87a0a9e3a26ca073a98af5523247", "6f314c1d10090f43422756a0451509a8481d87a0a9e3a26ca073a98af5523247") shouldBe true
  }

  "compare" should "return false for 2 different sha256 strings" in {
    ChecksumUtils
      .compare("6f314c1d10090f43422756a0451509a8481d87a0a9e3a26ca073a98af5523247", "6f314c1d10090f4340451509a8481d87a0a9e3a26ca073a98af5523247") shouldBe false
  }


}
