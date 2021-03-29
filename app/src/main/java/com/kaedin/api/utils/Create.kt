package com.kaedin.api.utils

import android.graphics.Bitmap
import com.kaedin.api.utils.DataUtils.getValueFromJsonObject
import com.kaedin.api.models.*
import org.json.JSONObject

class Create {

    companion object {

        fun launchTile(json: JSONObject): Launch {
            val launch = Launch()
            launch.id = getValueFromJsonObject(json, "id", String).toString()
            launch.name = getValueFromJsonObject(json, "name", String).toString()
            launch.flight_number = getValueFromJsonObject(json, "flight_number", Int) as Int
            launch.rocket_id = getValueFromJsonObject(json, "rocket", String).toString()
            launch.upcoming = getValueFromJsonObject(json, "upcoming", Boolean) as Boolean
            launch.date_unix = getValueFromJsonObject(json, "date_unix", Int) as Int
            launch.details = getValueFromJsonObject(json, "details", String).toString()
            launch.links = getValueFromJsonObject(json, "", Links()) as Links

            return launch
        }

        fun launchFull(json: JSONObject): Launch {
            val launch = launchTile(json)
            launch.tbd = getValueFromJsonObject(json, "tbd", Boolean) as Boolean
            launch.net = getValueFromJsonObject(json, "net", Boolean) as Boolean
            launch.window = getValueFromJsonObject(json, "window", Int) as? Int
            launch.success = getValueFromJsonObject(json, "success", Boolean) as Boolean
            launch.crew_ids = getValueFromJsonObject(json, "crew", ArrayList<String>()) as ArrayList<String>
            launch.ship_ids = getValueFromJsonObject(json, "ships", ArrayList<String>()) as ArrayList<String>
            launch.capsule_ids = getValueFromJsonObject(json, "capsules", ArrayList<String>()) as ArrayList<String>
            launch.payload_ids = getValueFromJsonObject(json, "payloads", ArrayList<String>()) as ArrayList<String>
            launch.launchpad_id = getValueFromJsonObject(json, "launchpad", String).toString()
            launch.auto_update = getValueFromJsonObject(json, "auto_update", Boolean) as Boolean
            launch.launch_library_id = getValueFromJsonObject(json, "launch_library_id", String).toString()
            launch.failures = getValueFromJsonObject(json, "failures", ArrayList<Failure>()) as ArrayList<Failure>
            launch.cores = getValueFromJsonObject(json, "cores", Core()) as? Core

            return launch
        }

        fun rocket(launchJSON: JSONObject) : Rocket {
            val rocket = Rocket()
            println("Launch details: $launchJSON")
            rocket.id = getValueFromJsonObject(launchJSON, "id", String).toString()
            rocket.name = getValueFromJsonObject(launchJSON, "name", String).toString()
            rocket.measurements = getValueFromJsonObject(launchJSON, "", RocketMeasurements()) as RocketMeasurements
            rocket.type = getValueFromJsonObject(launchJSON, "type", String).toString()
            rocket.active = getValueFromJsonObject(launchJSON, "active", Boolean) as Boolean
            rocket.stages = getValueFromJsonObject(launchJSON, "stages", Int) as Int
            rocket.boosters = getValueFromJsonObject(launchJSON, "boosters", Int) as Int
            rocket.cost_per_launch = getValueFromJsonObject(launchJSON, "cost_per_launch", Int) as Int
            rocket.success_rate_pct = getValueFromJsonObject(launchJSON, "success_rate_pct", Double) as Double
            rocket.first_flight = getValueFromJsonObject(launchJSON, "first_flight", String).toString()
            rocket.country = getValueFromJsonObject(launchJSON, "country", String).toString()
            rocket.company = getValueFromJsonObject(launchJSON, "company", String).toString()
            rocket.wikipedia_link = getValueFromJsonObject(launchJSON, "wikipedia", String).toString()
            rocket.description = getValueFromJsonObject(launchJSON, "description", String).toString()
            rocket.flickr_images = getValueFromJsonObject(launchJSON, "flickr_images", ArrayList<String>()) as ArrayList<String>
            rocket.rocketFirstStage = getValueFromJsonObject(launchJSON, "", RocketFirstStage()) as RocketFirstStage
            rocket.rocketSecondStage = getValueFromJsonObject(launchJSON, "", RocketSecondStage()) as RocketSecondStage
            rocket.engines = getValueFromJsonObject(launchJSON, "", Engines()) as Engines
            rocket.landingLegs = getValueFromJsonObject(launchJSON, "", LandingLegs()) as LandingLegs
            return rocket
        }

//        fun launchDetail(launchJSON: JSONObject): LaunchDetails {
//            var id = -1
//            var mission = ""
//            var rocketName = ""
//            var launchDate = ""
//            var launchSite = ""
//            var success = ""
//            var details = ""
//            var missions_small_patch: Bitmap? = null
//
//
//            val rocketJson = launchJSON.getJSONObject("rocket")
//            val launchSiteJson = launchJSON.getJSONObject("launch_site")
//            val linksJson = launchJSON.getJSONObject("links")
//
//            success = launchJSON.getBoolean("launch_success").toString()
////            val links = getLinks(linksJson)
//            val links = Links()
//            id = launchJSON.getInt("flight_number")
//            mission = launchJSON.getString("mission_name")
//
//            launchDate =
//                    /*DataUtils.getDateTime(launchJSON.getString("launch_date_unix")).toString()*/
//                "date"
//
//            if (launchDate == "null") launchDate = "No date available"
//            launchSite = launchSiteJson.getString("site_name_long")
//            rocketName = rocketJson.getString("rocket_name")
//            details = launchJSON.getString("details")
//
//            missions_small_patch =
//                DataUtils.getBitmapFromString(linksJson.getString("mission_patch_small"))
//
//            val launch = LaunchDetails(
//                id,
//                details,
//                mission,
//                missions_small_patch,
//                rocketName,
//                launchDate,
//                launchSite,
//                success,
//                links
//            )
//
//            return launch
//        }

//        fun getLinks(linksJSON: JSONObject): Links {
//            val yt = linksJSON.getString("youtube_id")
//            val reddit = linksJSON.getString("reddit_media")
//            val news = linksJSON.getString("article_link")
//            val wiki = linksJSON.getString("wikipedia")
//            val press = linksJSON.getString("presskit")
//
//            val links = Links(
//                yt,
//                reddit,
//                news,
//                wiki,
//                press
//            )
//            return links
//        }
    }
}