package com.kaedin.spacex.utils

import com.kaedin.spacex.models.*
import com.kaedin.spacex.models.Rocket.RocketMeasurements
import com.kaedin.spacex.utils.DataUtils.getValueFromJsonObject
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
        val coresJson = json.getJSONArray("cores")
        val coreIds = ArrayList<String>()
        for (i in 0 until coresJson.length()){
            val value = coresJson.getJSONObject(i).getString("core")
            if (value != "null") {
                coreIds.add(value)
            }
        }
        launch.cores = coreIds
        launch.payload_ids =
            getValueFromJsonObject(json, "payloads", ArrayList<String>()) as ArrayList<String>
        launch.ship_ids =
            getValueFromJsonObject(json, "ships", ArrayList<String>()) as ArrayList<String>
        launch.capsule_ids =
            getValueFromJsonObject(json, "capsules", ArrayList<String>()) as ArrayList<String>

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

    fun payloadTile(json: JSONObject): Payload {
        val payload = Payload()
        payload.name = getValueFromJsonObject(json, "name", String).toString()
        payload.id = getValueFromJsonObject(json, "id", String).toString()
        payload.type = getValueFromJsonObject(json, "type", String).toString()
        return payload
    }

    fun payload(json: JSONObject): Payload {
        val payload = payloadTile(json)
        payload.reused = getValueFromJsonObject(json, "reused", Boolean) as Boolean
        payload.launch = getValueFromJsonObject(json, "launch", String).toString()
        payload.customers =
            getValueFromJsonObject(json, "customers", ArrayList<String>()) as ArrayList<String>
        payload.norad_ids =
            getValueFromJsonObject(json, "norad_ids", ArrayList<String>()) as ArrayList<String>
        payload.nationalities =
            getValueFromJsonObject(json, "nationalities", ArrayList<String>()) as ArrayList<String>
        payload.manufacturers =
            getValueFromJsonObject(json, "manufacturers", ArrayList<String>()) as ArrayList<String>
        payload.mass = getValueFromJsonObject(json, "mass_kg", Int) as? Int
        payload.orbit = getValueFromJsonObject(json, "orbit", String).toString()
        payload.referenceSystem =
            getValueFromJsonObject(json, "reference_system", String).toString()
        payload.regime = getValueFromJsonObject(json, "regime", String).toString()
        payload.eccentricity = getValueFromJsonObject(json, "eccentricity", Double) as? Double
        payload.epoch = getValueFromJsonObject(json, "epoch", String).toString()
        payload.lifespanYears = getValueFromJsonObject(json, "lifespan_years", Int) as? Int
        return payload
    }

    fun shipTile(json: JSONObject): Ship {
        val ship = Ship()
        ship.id = getValueFromJsonObject(json, "id", String).toString()
        ship.name = getValueFromJsonObject(json, "name", String).toString()
        ship.image = getValueFromJsonObject(json, "image", String).toString()
        ship.type = getValueFromJsonObject(json, "type", String).toString()
        return ship
    }

    fun ship(json: JSONObject): Ship {
        val ship = shipTile(json)
        ship.legacyId = getValueFromJsonObject(json, "legacy_id", String).toString()
        ship.active = getValueFromJsonObject(json, "active", Boolean) as? Boolean
        ship.model = getValueFromJsonObject(json, "model", String).toString()
        ship.roles = getValueFromJsonObject(json, "roles", ArrayList<String>()) as ArrayList<String>
        ship.imo = getValueFromJsonObject(json, "imo", Int) as? Int
        ship.mmsi = getValueFromJsonObject(json, "mmsi", Int) as? Int
        ship.abs = getValueFromJsonObject(json, "abs", Int) as? Int
        ship.shipClass = getValueFromJsonObject(json, "class", Int) as? Int
        ship.mass = getValueFromJsonObject(json, "mass", Int) as? Int
        ship.buildYear = getValueFromJsonObject(json, "year_built", Int) as? Int
        ship.homePort = getValueFromJsonObject(json, "home_port", String).toString()
        ship.status = getValueFromJsonObject(json, "status", String).toString()
        ship.speedKN = getValueFromJsonObject(json, "speed_kn", Int) as? Int
        ship.latitude = getValueFromJsonObject(json, "latitude", Double) as? Double
        ship.longitude = getValueFromJsonObject(json, "longitude", Double) as? Double
        ship.lastAisUpdate = getValueFromJsonObject(json, "last_ais_update", String).toString()
        ship.marineTrafficLink = getValueFromJsonObject(json, "link", String).toString()
        ship.launches =
            getValueFromJsonObject(json, "launches", ArrayList<String>()) as ArrayList<String>

        return ship
    }

    fun capsule(json: JSONObject): Capsule {
        val capsule = Capsule()
        capsule.reusedCount = getValueFromJsonObject(json, "reused_count", Int) as? Int
        capsule.waterLandings = getValueFromJsonObject(json, "water_landings", Int) as? Int
        capsule.landLandings = getValueFromJsonObject(json, "land_landings", Int) as? Int
        capsule.lastUpdate = getValueFromJsonObject(json, "last_update", String).toString()
        capsule.launches =
            getValueFromJsonObject(json, "launches", ArrayList<String>()) as ArrayList<String>
        capsule.serial = getValueFromJsonObject(json, "serial", String).toString()
        capsule.status = getValueFromJsonObject(json, "status", String).toString()
        capsule.type = getValueFromJsonObject(json, "type", String).toString()
        capsule.id = getValueFromJsonObject(json, "id", String).toString()
        return capsule
    }

    fun core(json: JSONObject): Core {
        val core = Core()
        core.block = getValueFromJsonObject(json, "block", Int) as? Int
        core.reuseCount = getValueFromJsonObject(json, "reuse_count", Int) as? Int
        core.rltsAttempts = getValueFromJsonObject(json, "rlts_attempts", Int) as? Int
        core.rltsLandings = getValueFromJsonObject(json, "rlts_landings", Int) as? Int
        core.asdsAttempts = getValueFromJsonObject(json, "asds_attempts", Int) as? Int
        core.asdsLandings = getValueFromJsonObject(json, "asds_landings", Int) as? Int
        core.lastUpdate = getValueFromJsonObject(json, "last_update", String).toString()
        core.launchIds =
            getValueFromJsonObject(json, "launches", ArrayList<String>()) as? ArrayList<String>
        core.serial = getValueFromJsonObject(json, "serial", String).toString()
        core.status = getValueFromJsonObject(json, "status", String).toString()
        core.id = getValueFromJsonObject(json, "id", String).toString()
        return core
    }
}
