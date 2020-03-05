package utils

import org.scalacheck._
import org.scalacheck.Gen
import org.scalatest.prop.PropertyChecks

trait GeneratorHelper extends PropertyChecks {

  // The values used as bounds for generation

  private val inBoundsLatitudeNorth: Double = 52.2300
  private val inBoundsLatitudeSouth: Double = 50.7840

  private val outOfBoundsLatitudeNorth: Double = 52.232
  private val outOfBoundsLatitudeSouth: Double = 50.7838

  private val inBoundsLongitudeEast = 1.2901
  private val inBoundsLongitudeWest = -1.0345

  private val outOfBoundsLongitudeEast = 1.2906
  private val outOfBoundsLongitudeWest = -1.0351

  def inBoundsLatitudeGen: Gen[Double] =
    Gen.chooseNum[Double](inBoundsLatitudeSouth, inBoundsLatitudeNorth)

  def inBoundsLongitudeGen: Gen[Double] =
    Gen.chooseNum[Double](inBoundsLongitudeWest, inBoundsLongitudeEast)

  def outOfBoundsLatitudeGen: Gen[Double] =
    Arbitrary.arbitrary[Double] suchThat {
      chosen => chosen < outOfBoundsLatitudeSouth || chosen > outOfBoundsLatitudeNorth
    }

  def outOfBoundsLongitudeGen: Gen[Double] =
    Arbitrary.arbitrary[Double] suchThat {
      chosen => chosen < outOfBoundsLongitudeWest || chosen > outOfBoundsLongitudeEast
    }

  def bigDecimalGen: Gen[BigDecimal] =
    Arbitrary.arbitrary[BigDecimal]
}
