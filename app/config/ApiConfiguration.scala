package config

import javax.inject.Inject
import play.api.Configuration

class ApiConfiguration @Inject()(config: Configuration) {

  private val apiBaseUrlKey: String = "bpdts-api.base-url"

  val apiBaseUrl: String = config.getString(apiBaseUrlKey)
    .getOrElse(throw new RuntimeException(s"Could not retrieve '$apiBaseUrlKey' from application.conf"))
}
