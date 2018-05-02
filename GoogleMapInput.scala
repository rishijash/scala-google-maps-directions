package data

import play.api.libs.json.Json

/**
  * Created by rishi on 1/28/18.
  */


case class GoogleMapInput(source: String, destination: String, mode: String, departureTime: Long)

object GoogleMapInput {
  implicit val userJsonFormat = Json.format[GoogleMapInput]
}
