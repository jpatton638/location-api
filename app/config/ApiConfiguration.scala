package config

import javax.inject.Inject
import play.api.Configuration

class ApiConfiguration @Inject()(config: Configuration) {

  private val apiBaseUrlKey: String = "bpdts-api.base-url"
  private val londonCoordinatesKey: String = "london-coordinates"

  def apiBaseUrl: String = config.getString(apiBaseUrlKey)
    .getOrElse(throw new RuntimeException(
      s"Could not retrieve '$apiBaseUrlKey' from application.conf"
    ))

  def londonLatitude: Double = config.getDouble(s"$londonCoordinatesKey.latitude")
    .getOrElse(throw new RuntimeException(
      s"Could not retrieve '$londonCoordinatesKey-latitude' from application.conf"
    ))

  def londonLongitude: Double = config.getDouble(s"$londonCoordinatesKey.longitude")
    .getOrElse(throw new RuntimeException(
      s"Could not retrieve '$londonCoordinatesKey-longitude' from application.conf"
    ))
}
