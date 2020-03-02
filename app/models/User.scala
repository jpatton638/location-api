package models

import play.api.libs.json.{Json, OFormat}

case class User(
               id: Int,
               first_name: String,
               last_name: String,
               email: String,
               ip_address: String,
               latitude: BigDecimal,
               longitude: BigDecimal,
               city: Option[String]
               )

object User {
  implicit val formats: OFormat[User] = Json.format[User]
}
