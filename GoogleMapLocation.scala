package data

import play.api.libs.json.Json

/**
  * Created by rishi on 1/28/18.
  */
case class GoogleMapLocation(latitude: Double, longitude: Double)

object GoogleMapLocation {
  implicit val userJsonFormat = Json.format[GoogleMapLocation]
}
