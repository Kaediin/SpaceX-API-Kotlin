package com.kaedin.api.utils

import com.kaedin.api.utils.DataUtils.getValueFromJsonObject
import com.kaedin.api.models.*
import com.kaedin.api.models.Rocket.RocketMeasurements
import org.json.JSONObject

object Create {

    fun dragonTile(json: JSONObject): Dragon {
        val dragon = Dragon()
        dragon.flickrImages =
            getValueFromJsonObject(json, "flickr_images", ArrayList<String>()) as ArrayList<String>
        dragon.name = getValueFromJsonObject(json, "name", String).toString()
        dragon.id = getValueFromJsonObject(json, "id", String).toString()
        return dragon
    }

    fun dragon(json: JSONObject): Dragon {
        val dragon = dragonTile(json)
        dragon.heatShield = getValueFromJsonObject(
            json.getJSONObject("heat_shield"),
            "heat_shield",
            Dragon.HeatShield()
        ) as Dragon.HeatShield
        dragon.launchPayloadMass =
            getValueFromJsonObject(json.getJSONObject("launch_payload_mass"), "kg", Int) as Int
        dragon.launchPayloadVolume = getValueFromJsonObject(
            json.getJSONObject("launch_payload_vol"),
            "cubic_meters",
            Int
        ) as Int
        dragon.returnPayloadMass =
            getValueFromJsonObject(json.getJSONObject("return_payload_mass"), "kg", Int) as Int
        dragon.returnPayloadVolume = getValueFromJsonObject(
            json.getJSONObject("return_payload_vol"),
            "cubic_meters",
            Int
        ) as Int
        dragon.trunk = getValueFromJsonObject(
            json.getJSONObject("trunk"),
            "trunk",
            Dragon.Trunk()
        ) as Dragon.Trunk
        dragon.height =
            getValueFromJsonObject(json.getJSONObject("height_w_trunk"), "meters", Double) as Double
        dragon.diameter =
            getValueFromJsonObject(json.getJSONObject("diameter"), "meters", Double) as Double
        dragon.firstFlight = getValueFromJsonObject(json, "first_flight", String) as String
        dragon.type = getValueFromJsonObject(json, "type", String).toString()
        dragon.active = getValueFromJsonObject(json, "active", Boolean) as Boolean
        dragon.crewCapacity = getValueFromJsonObject(json, "crew_capacity", Int) as Int
        dragon.orbitDurationYears = getValueFromJsonObject(json, "orbit_duration_yr", Int) as Int
        dragon.thursters = getValueFromJsonObject(
            json,
            "thrusters",
            ArrayList<Dragon.Thruster>()
        ) as ArrayList<Dragon.Thruster>
        dragon.wikipedia = getValueFromJsonObject(json, "wikipedia", String).toString()
        dragon.description = getValueFromJsonObject(json, "description", String).toString()
        return dragon
    }

    fun launchTile(json: JSONObject): Launch {
        val launch = Launch()
        launch.id = getValueFromJsonObject(json, "id", String).toString()
        launch.name = getValueFromJsonObject(json, "name", String).toString()
        launch.flight_number = getValueFromJsonObject(json, "flight_number", Int) as Int
        launch.rocket_id = getValueFromJsonObject(json, "rocket", String).toString()
        launch.upcoming = getValueFromJsonObject(json, "upcoming", Boolean) as Boolean
        launch.date_unix = getValueFromJsonObject(json, "date_unix", Int) as Int
        launch.details = getValueFromJsonObject(json, "details", String).toString()
        launch.links = getValueFromJsonObject(json, "", Launch.Links()) as Launch.Links
        launch.launchpad_id = getValueFromJsonObject(json, "launchpad", String).toString()
        launch.cores = getValueFromJsonObject(json, "cores", ArrayList<Core>()) as? ArrayList<Core>

        return launch
    }

    fun launch(json: JSONObject): Launch {
        val launch = launchTile(json)
        launch.tbd = getValueFromJsonObject(json, "tbd", Boolean) as Boolean
        launch.net = getValueFromJsonObject(json, "net", Boolean) as Boolean
        launch.window = getValueFromJsonObject(json, "window", Int) as? Int
        launch.success = getValueFromJsonObject(json, "success", Boolean) as Boolean
        launch.crew_ids =
            getValueFromJsonObject(json, "crew", ArrayList<String>()) as ArrayList<String>
        launch.ship_ids =
            getValueFromJsonObject(json, "ships", ArrayList<String>()) as ArrayList<String>
        launch.capsule_ids =
            getValueFromJsonObject(json, "capsules", ArrayList<String>()) as ArrayList<String>
        launch.payload_ids =
            getValueFromJsonObject(json, "payloads", ArrayList<String>()) as ArrayList<String>
        launch.auto_update = getValueFromJsonObject(json, "auto_update", Boolean) as Boolean
        launch.launch_library_id =
            getValueFromJsonObject(json, "launch_library_id", String).toString()
        launch.failures = getValueFromJsonObject(
            json,
            "failures",
            ArrayList<Launch.Failure>()
        ) as ArrayList<Launch.Failure>

        return launch
    }

    fun rocketTile(launchJSON: JSONObject): Rocket {
        val rocket = Rocket()
        rocket.id = getValueFromJsonObject(launchJSON, "id", String).toString()
        rocket.name = getValueFromJsonObject(launchJSON, "name", String).toString()
        rocket.flickr_images = getValueFromJsonObject(
            launchJSON,
            "flickr_images",
            ArrayList<String>()
        ) as ArrayList<String>
        return rocket
    }

    fun rocket(json: JSONObject): Rocket {
        val rocket = rocketTile(json)
        rocket.measurements =
            getValueFromJsonObject(json, "", RocketMeasurements()) as RocketMeasurements
        rocket.type = getValueFromJsonObject(json, "type", String).toString()
        rocket.active = getValueFromJsonObject(json, "active", Boolean) as Boolean
        rocket.stages = getValueFromJsonObject(json, "stages", Int) as Int
        rocket.boosters = getValueFromJsonObject(json, "boosters", Int) as Int
        rocket.cost_per_launch = getValueFromJsonObject(json, "cost_per_launch", Int) as Int
        rocket.success_rate_pct = getValueFromJsonObject(json, "success_rate_pct", Double) as Double
        rocket.first_flight = getValueFromJsonObject(json, "first_flight", String).toString()
        rocket.country = getValueFromJsonObject(json, "country", String).toString()
        rocket.company = getValueFromJsonObject(json, "company", String).toString()
        rocket.wikipedia_link = getValueFromJsonObject(json, "wikipedia", String).toString()
        rocket.description = getValueFromJsonObject(json, "description", String).toString()
        rocket.rocketFirstStage =
            getValueFromJsonObject(json, "", Rocket.RocketFirstStage()) as Rocket.RocketFirstStage
        rocket.rocketSecondStage =
            getValueFromJsonObject(json, "", Rocket.RocketSecondStage()) as Rocket.RocketSecondStage
        rocket.engine = getValueFromJsonObject(json, "", Rocket.Engine()) as Rocket.Engine
        rocket.landingLegs =
            getValueFromJsonObject(json, "", Rocket.LandingLegs()) as Rocket.LandingLegs
        return rocket
    }

    fun landpadTile(landpadJSON: JSONObject): Landpad {
        val landpad = Landpad()
        landpad.name = getValueFromJsonObject(landpadJSON, "name", String).toString()
        landpad.fullName = getValueFromJsonObject(landpadJSON, "full_name", String).toString()
        landpad.locality = getValueFromJsonObject(landpadJSON, "locality", String).toString()
        landpad.region = getValueFromJsonObject(landpadJSON, "region", String).toString()
        landpad.latitude = getValueFromJsonObject(landpadJSON, "latitude", Double) as Double
        landpad.longitude = getValueFromJsonObject(landpadJSON, "longitude", Double) as Double
        landpad.id = getValueFromJsonObject(landpadJSON, "id", String).toString()
        return landpad
    }

    fun landpad(landpadJSON: JSONObject): Landpad {
        val landpad = landpadTile(landpadJSON)
        landpad.type = getValueFromJsonObject(landpadJSON, "type", String).toString()
        landpad.landingAttempts =
            getValueFromJsonObject(landpadJSON, "landing_attempts", Int) as Int
        landpad.landingSuccesses =
            getValueFromJsonObject(landpadJSON, "landing_successes", Int) as Int
        landpad.wikipedia = getValueFromJsonObject(landpadJSON, "wikipedia", String).toString()
        landpad.details = getValueFromJsonObject(landpadJSON, "details", String).toString()
        landpad.launchIds = getValueFromJsonObject(
            landpadJSON,
            "launches",
            ArrayList<String>()
        ) as ArrayList<String>
        landpad.status = getValueFromJsonObject(landpadJSON, "status", String).toString()
        return landpad
    }

    fun launchpadTile(launchpadJSON: JSONObject): Launchpad {
        val launchpad = Launchpad()
        launchpad.name = getValueFromJsonObject(launchpadJSON, "name", String).toString()
        launchpad.fullName = getValueFromJsonObject(launchpadJSON, "full_name", String).toString()
        launchpad.locality = getValueFromJsonObject(launchpadJSON, "locality", String).toString()
        launchpad.region = getValueFromJsonObject(launchpadJSON, "region", String).toString()
        launchpad.latitude = getValueFromJsonObject(launchpadJSON, "latitude", Double) as Double
        launchpad.longitude = getValueFromJsonObject(launchpadJSON, "longitude", Double) as Double
        launchpad.id = getValueFromJsonObject(launchpadJSON, "id", String).toString()
        return launchpad
    }

    fun launchpad(launchpadJSON: JSONObject): Launchpad {
        val launchpad = launchpadTile(launchpadJSON)
        launchpad.type = getValueFromJsonObject(launchpadJSON, "type", String).toString()
        launchpad.landingAttempts =
            getValueFromJsonObject(launchpadJSON, "launch_attempts", Int) as Int
        launchpad.landingSuccesses =
            getValueFromJsonObject(launchpadJSON, "launch_successes", Int) as Int
        launchpad.wikipedia = getValueFromJsonObject(launchpadJSON, "wikipedia", String).toString()
        launchpad.details = getValueFromJsonObject(launchpadJSON, "details", String).toString()
        launchpad.rocketIds = getValueFromJsonObject(
            launchpadJSON,
            "rockets",
            ArrayList<String>()
        ) as ArrayList<String>
        launchpad.launchIds = getValueFromJsonObject(
            launchpadJSON,
            "launches",
            ArrayList<String>()
        ) as ArrayList<String>
        launchpad.status = getValueFromJsonObject(launchpadJSON, "status", String).toString()
        return launchpad
    }
}
