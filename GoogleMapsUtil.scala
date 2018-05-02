package Helper.GoogleMaps

import data.GoogleMapInput

import scala.collection.mutable.ListBuffer

/**
  * Created by rishi on 1/28/18.
  */
object Util {

  val API_KEY = ""

  def get(url: String) = scala.io.Source.fromURL(url).mkString

  def getUrl(googleMapInput: GoogleMapInput): String ={
    "https://maps.googleapis.com/maps/api/directions/json?origin="+ googleMapInput.source + "&destination="+googleMapInput.destination+"&key="+ API_KEY+"&mode="+googleMapInput.mode+"&departure_time="+googleMapInput.departureTime
  }

  def ListBufferToJSONArray(list: ListBuffer[String]): String ={
    var jsonarraystr = ""
    jsonarraystr = jsonarraystr + "["
    list.foreach {
      list_sub =>
        jsonarraystr = jsonarraystr + list_sub.toString
        jsonarraystr = jsonarraystr + ","
    }
    jsonarraystr = jsonarraystr.substring(0, jsonarraystr.length - 1)
    jsonarraystr = jsonarraystr + "]"
    jsonarraystr
  }



}
