package com.example.api

import Payload
import android.graphics.Bitmap
import com.example.api.DataUtils.Companion.getStringFromJsonObject
import com.example.api.fragments.rocketFragment
import com.example.api.model.*
import org.json.JSONException
import org.json.JSONObject

class Create {

    companion object {

        fun launchTile(
            filter: Filter,
            launchJSON: JSONObject,
            linksJSON: JSONObject,
            rocketJSON: JSONObject
        ): Launch {
            // Launch object vars
            val id = launchJSON.getInt("flight_number")
            val details = launchJSON.getString("details")
            val mission = launchJSON.getString("mission_name")
            var mission_patch_small: Bitmap? = null

            if (!filter.lowQuality) {
                mission_patch_small =
                    DataUtils.getBitmapFromString(linksJSON.getString("mission_patch_small"))
            }

            val rocket = rocketJSON.getString("rocket_name")
            val upcoming = launchJSON.getBoolean("upcoming")
            var launch_date = DataUtils.getDateTime(launchJSON.getString("launch_date_unix"))

            if (launch_date == null) launch_date = "No date available"
//            println(id)
            val launch = Launch(
                id,
                details,
                mission,
                mission_patch_small,
                rocket,
                upcoming,
                launch_date
            )

            return launch
        }

        fun rocket(launchJSON: JSONObject) : Rocket {
            var firstStage: FirstStage? = null
            var secondStage: SecondStage? = null
            var fairing: Fairing? = null

            try {
                val rocketJSON: JSONObject = launchJSON.getJSONObject("rocket")

                try {
                    val firstStageJSON = rocketJSON.getJSONObject("first_stage")
                    val coresArray = firstStageJSON.getJSONArray("cores")
                    val cores = ArrayList<Core>()
                    for (i in 0 until coresArray.length()) {

                        // First stage
                        try {
                            val coreJSON = coresArray.getJSONObject(i)

                            val code_serial =
                                getStringFromJsonObject(coreJSON, "core_serial", String)
                            val flight =
                                getStringFromJsonObject(coreJSON, "flight", String)
                            val block = getStringFromJsonObject(coreJSON, "block", Int)
                            val gridfins =
                                getStringFromJsonObject(coreJSON, "gridfins", Boolean)
                            val legs =
                                getStringFromJsonObject(coreJSON, "legs", Boolean)
                            val reused =
                                getStringFromJsonObject(coreJSON, "reused", Boolean)
                            val land_success = getStringFromJsonObject(
                                coreJSON,
                                "land_success",
                                Boolean
                            )
                            val landing_intent = getStringFromJsonObject(
                                coreJSON,
                                "landing_intent",
                                Boolean
                            )
                            val landing_type = getStringFromJsonObject(
                                coreJSON,
                                "landing_type",
                                String
                            )
                            val landing_vehicle = getStringFromJsonObject(
                                coreJSON,
                                "landing_vehicle",
                                String
                            )

                            val core = Core(
                                code_serial,
                                flight,
                                block,
                                gridfins,
                                legs,
                                reused,
                                land_success,
                                landing_intent,
                                landing_type,
                                landing_vehicle
                            )
                            cores.add(core)
                        } catch (e: JSONException) {
                            println("1: "+e)
                        }
                    }
                    firstStage = FirstStage(cores)
                } catch (e: JSONException) {
                    println("2"+ e)
                }

                // Second stage
                try {
                    val secondStageJSON = rocketJSON.getJSONObject("second_stage")

                    try {
                        val payloadsJSON = secondStageJSON.getJSONArray("payloads")
                        val payloads = ArrayList<Payload>()
                        for (i in 0 until payloadsJSON.length()) {
                            try {
                                println("line 1")
                                val payloadJSON = payloadsJSON.getJSONObject(i)

                                val payload_id = getStringFromJsonObject(
                                    payloadJSON,
                                    "payload_id",
                                    String
                                )
                                val reused = getStringFromJsonObject(
                                    payloadJSON,
                                    "reused",
                                    Boolean
                                )

                                val customers = DataUtils.getStringFromJSONArray(
                                    payloadJSON.getJSONArray("customers")
                                )

                                val nationality = getStringFromJsonObject(
                                    payloadJSON,
                                    "nationality",
                                    String
                                )
                                val manufacturer = getStringFromJsonObject(
                                    payloadJSON,
                                    "manufacturer",
                                    String
                                )
                                val payload_type = getStringFromJsonObject(
                                    payloadJSON,
                                    "payload_type",
                                    String
                                )
                                val payload_mass = getStringFromJsonObject(
                                    payloadJSON,
                                    "payload_mass_kg",
                                    Int
                                )
                                val orbit = getStringFromJsonObject(
                                    payloadJSON,
                                    "orbit",
                                    String
                                )
                                val payload = Payload(
                                    payload_id,
                                    reused,
                                    customers,
                                    nationality,
                                    manufacturer,
                                    payload_type,
                                    payload_mass,
                                    orbit
                                )

                                payloads.add(payload)
                            } catch (e: JSONException) {
                                println("3: "+e)
                            }
                        }
                        secondStage = SecondStage(payloads)
                    } catch (e: JSONException) {
                        println("4: "+e)
                    }
                } catch (e: JSONException) {
                    println("5: "+e)
                }

                try {
                    val fairingsJSON = rocketJSON.getJSONObject("fairings")

                    val reused = getStringFromJsonObject(fairingsJSON, "reused", String)
                    val recoveryAttempt = getStringFromJsonObject(
                        fairingsJSON,
                        "recovery_attempt",
                        String
                    )
                    val recovered = getStringFromJsonObject(
                        fairingsJSON,
                        "recovery_attempt",
                        String
                    )
                    val ship = getStringFromJsonObject(fairingsJSON, "ship", String)

/*
                                val reused = fairingsJSON.getString("reused").toString()
                                val recoveryAttempt =
                                    fairingsJSON.getString("recovery_attempt").toString()
                                val recovered = fairingsJSON.getString("recovered").toString()
                                val ship = fairingsJSON.getString("ship").toString()

 */

                    fairing = Fairing(reused, recoveryAttempt, recovered, ship)
                } catch (e: JSONException) {
                    fairing = Fairing(
                        "Not available",
                        "Not available",
                        "Not available",
                        "Not available"
                    )
                    println("6: "+e)
                }


            } catch (e: JSONException) {
                println("7: "+e)
            }

            val rocket = Rocket(firstStage, secondStage, fairing)
            return rocket
        }

        fun launchDetail(launchJSON: JSONObject): LaunchDetails {
            var id = -1
            var mission = ""
            var rocketName = ""
            var launchDate = ""
            var launchSite = ""
            var success = ""
            var details = ""
            var missions_small_patch: Bitmap? = null


            val rocketJson = launchJSON.getJSONObject("rocket")
            val launchSiteJson = launchJSON.getJSONObject("launch_site")
            val linksJson = launchJSON.getJSONObject("links")

            success = launchJSON.getBoolean("launch_success").toString()
            val links = getLinks(linksJson)

            id = launchJSON.getInt("flight_number")
            mission = launchJSON.getString("mission_name")

            launchDate =
                    /*DataUtils.getDateTime(launchJSON.getString("launch_date_unix")).toString()*/
                "date"

            if (launchDate == "null") launchDate = "No date available"
            launchSite = launchSiteJson.getString("site_name_long")
            rocketName = rocketJson.getString("rocket_name")
            details = launchJSON.getString("details")

            missions_small_patch =
                DataUtils.getBitmapFromString(linksJson.getString("mission_patch_small"))

            val launch = LaunchDetails(
                id,
                details,
                mission,
                missions_small_patch,
                rocketName,
                launchDate,
                launchSite,
                success,
                links
            )

            return launch
        }

        fun getLinks(linksJSON: JSONObject): Links {
            val yt = linksJSON.getString("youtube_id")
            val reddit = linksJSON.getString("reddit_media")
            val news = linksJSON.getString("article_link")
            val wiki = linksJSON.getString("wikipedia")
            val press = linksJSON.getString("presskit")

            val links = Links(
                yt,
                reddit,
                news,
                wiki,
                press
            )
            return links
        }
    }
}