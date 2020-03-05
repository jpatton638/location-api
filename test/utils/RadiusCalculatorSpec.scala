package utils

import config.ApiConfiguration
import helpers.GeneratorHelper
import models.Coordinates
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite

class RadiusCalculatorSpec extends PlaySpec with GuiceOneAppPerSuite with GeneratorHelper {

  private lazy val config: ApiConfiguration = app.injector.instanceOf[ApiConfiguration]

  private val londonLatitude: Double = config.londonLatitude
  private val londonLongitude: Double = config.londonLongitude

  private val controllerUnderTest: RadiusCalculator = new RadiusCalculator(config)

  private val radius: Double = 50.0

  "RadiusCalculator.isWithinRadius" must {

    "return true" when {

      "the latitude is within 3 degrees and 50 miles or less from the given radius" in {

        forAll (inBoundsLatitudeGen) { lat =>
          controllerUnderTest.isWithinRadius(Coordinates(lat, londonLongitude), radius) mustBe true
        }
      }

      "the longitude is within 3 degrees and 50 miles or less from the given radius" in {
        forAll (inBoundsLongitudeGen) { long =>
          controllerUnderTest.isWithinRadius(Coordinates(londonLatitude, long), radius) mustBe true
        }
      }
    }

    "return false" when {

      "the latitude is outside 3 degrees" in {
        controllerUnderTest.isWithinRadius(Coordinates(londonLatitude + 3.1, londonLongitude), radius) mustBe false
      }

      "the longitude is outside 3 degrees" in {
        controllerUnderTest.isWithinRadius(Coordinates(londonLatitude, londonLongitude + 3.1), radius) mustBe false
      }

      "the latitude is outside 50 miles" in {
        forAll (outOfBoundsLatitudeGen) { lat =>
          controllerUnderTest.isWithinRadius(Coordinates(lat, londonLongitude), radius) mustBe false
        }
      }

      "the longitude is outside 50 miles" in {
        forAll (outOfBoundsLongitudeGen) { long =>
          controllerUnderTest.isWithinRadius(Coordinates(londonLatitude, long), radius) mustBe false
        }
      }

      "the result of the distance calculation is not a number" in {
        controllerUnderTest.isWithinRadius(Coordinates(Double.PositiveInfinity, Double.PositiveInfinity), radius) mustBe false
        controllerUnderTest.isWithinRadius(Coordinates(Double.PositiveInfinity, Double.NegativeInfinity), radius) mustBe false
        controllerUnderTest.isWithinRadius(Coordinates(Double.NegativeInfinity, Double.PositiveInfinity), radius) mustBe false
        controllerUnderTest.isWithinRadius(Coordinates(Double.NegativeInfinity, Double.NegativeInfinity), radius) mustBe false
      }
    }
  }
}
