package models

import org.scalatestplus.play.PlaySpec
import utils.GeneratorHelper

class CoordinatesSpec extends PlaySpec with GeneratorHelper {

  "Coordinates" must {
    "instantiate" when {
      "apply is called with BigDecimal parameters" in {
        forAll (bigDecimalGen, bigDecimalGen) { (lat, long) =>
          Coordinates(lat, long) mustBe a [Coordinates]
        }
      }
    }
  }
}
