package models

case class Coordinates(latitude: Double, longitude: Double)

object Coordinates {
  def apply(latitude: BigDecimal, longitude: BigDecimal): Coordinates =
    new Coordinates(latitude.doubleValue(), longitude.doubleValue())
}