Build awesome things with Google Maps for Scala! 

### Getting Started

* Sign up for Developer Account [Google Maps](https://developers.google.com/maps/)
* Download [Scala-Google-Maps-Directions](https://github.com/rishijash/scala-google-maps-directions)
* View the full [Google Maps API documentation](https://developers.google.com/maps/documentation/) to better familiarize yourself with the API

### How to Setup

* Download [Scala-Google-Maps-Directions](https://github.com/rishijash/scala-google-maps-directions) from Github
* Import GoogleMapsClient.scala, GoogleMapsUtil.scala, GoogleMapInput.scala, GoogleMapLocation.scala to your project.

### Example Usage

```
//Initialize
  val util = GoogleMapsUtil
  util.API_KEY = <your-api-key>
  var googleMapInput = GoogleMapInput(<source address or lat/lon>,<destination address or lat/lon>, <travel mode>, <unix departure time>)
  var link = util.getUrl(googleMapInput)
  var mapdatastring = util.get(link)
  val googlemapclient = new GoogleMapsClient(mapdatastring)
  
//Perform Operations on Directions
  if(googlemapclient.validResponse()){
    val steps = googlemapclient.getSteps(googlemapclient.getRouteLeg(googlemapclient.getBestRoute()))
    val distance = googlemapclient.getTotalDistanceForBestRoute()
    val duration = googlemapclient.getTotalTimeForBestRoute()
    val steps_list = steps.as[List[String]]
   
  }
```
### Recent Project(s)

* [Fuelity App](https://play.google.com/store/apps/details?id=com.fuelity)

### Lisence

scala-google-maps-directions is licensed under the GNU LESSER GENERAL PUBLIC LICENSE. See the LICENSE file for details.
