package Helper.GoogleMaps

import play.api.libs.json.{JsObject, JsValue, Json}
import data.{GoogleMapLocation}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer


/**
  * Created by rishi on 1/28/18.
  */
class GoogleMapsClient(mapdatastring: String) {

  val util = Util
  val mapdata: JsValue = Json.parse(mapdatastring)

  def getRoutes(): JsValue ={
    (mapdata \ "routes").asOpt[JsValue].get
  }

  def validResponse(): Boolean ={
    if(getRoutes().as[List[JsObject]].size>0){
      return true
    }
    return false
  }

  def getBestRoute(): JsValue = {
    val routes = getRoutes()
    val routes_list = routes.as[List[JsObject]]
    val bestroute = routes_list(0)
    bestroute
  }

  def getRouteLeg(myroute: JsValue): JsValue ={
    val legs = (myroute \ "legs").asOpt[JsValue].get
    val leg_list = legs.as[List[JsObject]]
    val firstleg = leg_list(0)
    firstleg
  }

  def getTotalDistanceForBestRoute(): Int ={
    var distance = 0
    val leg = getRouteLeg(getBestRoute())
    val distance_json = (leg \ "distance").asOpt[JsValue].get
    distance = (distance_json \ "value").asOpt[Int].get
    distance
  }

  def getTotalTimeForBestRoute(): Int ={
    var duration = 0
    val leg = getRouteLeg(getBestRoute())
    val duration_json = (leg \ "duration").asOpt[JsValue].get
    duration = (duration_json \ "value").asOpt[Int].get
    duration
  }

  def getTotalDistanceFromStep(step: JsValue): Int ={
    var distance = 0
    val distance_json = (step \ "distance").asOpt[JsValue].get
    distance = (distance_json \ "value").asOpt[Int].get
    distance
  }

  def getTravelModeFromStep(step: JsValue): String ={
    (step \ "travel_mode").asOpt[String].getOrElse("")
  }

  def getTotalTimeFromStep(step: JsValue): Int ={
    var duration = 0
    val duration_json = (step \ "duration").asOpt[JsValue].get
    duration = (duration_json \ "value").asOpt[Int].get
    duration
  }

  def getSteps(routeLeg: JsValue) : JsValue = {
    (routeLeg \ "steps").asOpt[JsValue].get
  }

  def getHTMLInstructionForStep(step: JsValue) : String = {
    (step \ "html_instructions").asOpt[String].getOrElse("")
  }

  def isTransit(step: JsValue) : Boolean = {
    var mode = (step \ "travel_mode").asOpt[String].get
    if (mode.equals(util.modes(3)))
      return true
    return false
  }

  def getTransitArrivalTimeFromStep(step: JsValue) : Long = {
    val transit_details_json = (step \ "transit_details").asOpt[JsValue].get
    val transit_arrival_json = (transit_details_json \ "arrival_time").asOpt[JsValue].get
    val time = (transit_arrival_json \ "value").asOpt[Long].get
    time
  }

  def getarrivalTimeForBestRoute(departuretime: Long): Long ={
    var arrivaltime: Long= 0
    var last_transit_arrival_time : Long = 0
    var last_transit_location = -1
    var done = false
    val steps = getSteps(getRouteLeg(getBestRoute()))
    val steps_list = steps.as[List[JsObject]]
    for (i <- (steps_list.length-1) to 0 by -1; if(done==false)){
      val step = steps_list(i)
      if(isTransit(step)){
        last_transit_location = i
        last_transit_arrival_time = getTransitArrivalTimeFromStep(step)
        done = true
      }
    }
    arrivaltime = arrivaltime + last_transit_arrival_time
    if(arrivaltime==0)
      arrivaltime = departuretime
    for(i <- last_transit_location+1 to steps_list.length-1 by 1){
      val step = steps_list(i)
      arrivaltime = arrivaltime + getTotalTimeFromStep(step)
    }
    arrivaltime
  }

  def getarrivalTimeFromRoute(map_route: ListBuffer[String], departuretime: Long): Long ={
    var arrivaltime: Long= 0
    var last_transit_arrival_time : Long = 0
    var last_transit_location = 0
    var done = false
    for (i <- (map_route.length-1) to 0 by -1; if(done==false)){
      val step = Json.parse(map_route(i))
      if(isTransit(step)){
        last_transit_location = i
        last_transit_arrival_time = getTransitArrivalTimeFromStep(step)
        done = true
      }
    }
    arrivaltime = arrivaltime + last_transit_arrival_time
    if(arrivaltime==0){
      arrivaltime = departuretime
      last_transit_location = -1
    }
    for(i <- last_transit_location+1 to map_route.length-1 by 1){
      val step = Json.parse(map_route(i))
      arrivaltime = arrivaltime + getTotalTimeFromStep(step)
    }
    arrivaltime
  }

  def getLatLonFromStep(step: JsValue): mutable.HashMap[String, Location] = {
    var locations = new mutable.HashMap[String,Location]()
    var location_temp = (step \ "start_location").asOpt[JsValue].get
    val start_location = new Location((location_temp \ "lat").asOpt[Double].getOrElse(0.00), (location_temp \ "lng").asOpt[Double].getOrElse(0.00))
    location_temp = (step \ "end_location").asOpt[JsValue].get
    val end_location = new Location((location_temp \ "lat").asOpt[Double].getOrElse(0.00), (location_temp \ "lng").asOpt[Double].getOrElse(0.00))
    locations += ("start_location"-> start_location, "end_location" -> end_location)
  }
}
