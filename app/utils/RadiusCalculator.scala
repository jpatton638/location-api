package utils

import java.lang.Math._

import config.ApiConfiguration
import models.Coordinates
import javax.inject._

class RadiusCalculator @Inject()(config: ApiConfiguration) {

  /*
    The logic for the calculation of the distance between latitude and longitude (Haversine formula) can be
    found at the following address

    https://www.movable-type.co.uk/scripts/latlong.html
  */

  def isWithinRadius(coordinates: Coordinates, radius: Double): Boolean = {

    val londonCoordinates: Coordinates = Coordinates(config.londonLatitude, config.londonLongitude)

    val distance: Double = calculateDistanceInMiles(coordinates, londonCoordinates)

    if(isWithinApproximateRange(coordinates, londonCoordinates)) {
      abs(distance) <= radius
    } else false
  }

  private def isWithinApproximateRange(origin: Coordinates, target: Coordinates) = {

    val latDifference: Double = abs(origin.latitude - target.latitude)
    val longDifference: Double = abs(origin.longitude - target.longitude)

    if (latDifference <= 3 || longDifference <= 3) true else false
  }

  private def calculateDistanceInMiles(firstCoordinates: Coordinates, secondCoordinates: Coordinates): Double = {

    val earthAverageRadiusInMiles: Double = 3960.0

    val firstRadians: Double = toRadians(firstCoordinates.latitude)
    val secondRadians: Double = toRadians(secondCoordinates.latitude)
    val differenceLat: Double = toRadians(secondCoordinates.latitude - firstCoordinates.latitude)
    val differenceLong: Double = toRadians(secondCoordinates.longitude - firstCoordinates.longitude)

    val calculatedAngles: Double = sin(differenceLat / 2) * sin(differenceLat / 2) +
      cos(firstRadians) * cos(secondRadians) *
        sin(differenceLong / 2) * sin(differenceLong / 2)

    val angleFromCoordinates: Double = 2 * atan2(sqrt(calculatedAngles), sqrt(1 - calculatedAngles))

    earthAverageRadiusInMiles * angleFromCoordinates
  }
}
