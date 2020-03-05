package helpers

import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.prop.PropertyChecks

trait GeneratorHelper extends PropertyChecks {

  /* The values used as bounds for generation were calculated using this
     online calculator to give a close estimation for boundary values
   */

  private val inBoundsLatitudeNorth: Double = 52.2300
  private val inBoundsLatitudeSouth: Double = 50.7840

  private val outOfBoundsLatitudeNorth: Double = 52.232
  private val outOfBoundsLatitudeSouth: Double = 50.7838

  private val inBoundsLongitudeEast = 1.2901
  private val inBoundsLongitudeWest = -1.0345

  private val outOfBoundsLongitudeEast = 1.2906
  private val outOfBoundsLongitudeWest = -1.0351

  def doubleGen: Gen[Double] = Arbitrary.arbitrary[Double]

  def bigDecimalGen: Gen[BigDecimal] = Arbitrary.arbitrary[BigDecimal]

  def inBoundsLatitudeGen: Gen[Double] =
    Gen.chooseNum[Double](inBoundsLatitudeSouth, inBoundsLatitudeNorth)

  def inBoundsLongitudeGen: Gen[Double] =
    Gen.chooseNum[Double](inBoundsLongitudeWest, inBoundsLongitudeEast)

  def outOfBoundsLatitudeGen: Gen[Double] =
   doubleGen suchThat {
      chosen => chosen > outOfBoundsLatitudeSouth || chosen < outOfBoundsLatitudeNorth
    }

  def outOfBoundsLongitudeGen: Gen[Double] =
    doubleGen suchThat {
      chosen => chosen < outOfBoundsLongitudeWest || chosen > outOfBoundsLongitudeEast
    }

}
