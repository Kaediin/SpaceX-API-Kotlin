package com.kaedin.spacex.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.kaedin.spacex.asynctasks.LaunchesRequests
import com.kaedin.spacex.asynctasks.RocketsRequests
import com.kaedin.spacex.models.*
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@SuppressLint("SetTextI18n")
object DataUtils {

    fun sortOldestToNewest(launches: ArrayList<Launch>, reverse: Boolean): ArrayList<Launch> {
        launches.sortWith { o1, o2 ->
            o1.flight_number!!.compareTo(o2.flight_number!!)
        }
        if (reverse) {
            launches.reverse()
        }
        return launches
    }


    fun setRecyclerViewRockets(view: View, activity: ViewUtils, idList: ArrayList<String>?) {
        val jsonArray = JSONArray()
        if (idList != null) {
            val client = OkHttpClient()
            idList.forEachIndexed { i, id ->
                val url = "https://api.spacexdata.com/v4/rockets/$id"
                val request = Request.Builder()
                    .url(url)
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {

                    }

                    override fun onResponse(call: Call, response: Response) {
                        jsonArray.put(response.body()!!.string())
                        if ((i + 1) == idList.size) {
                            RocketsRequests(null, activity, view).execute(jsonArray)
                        }
                    }
                })
            }
        }
    }

    fun setRecyclerViewMissions(view: View, activity: ViewUtils, idList: ArrayList<String>?, value : Int) {
        val jsonArray = JSONArray()
        if (idList != null) {
            val client = OkHttpClient()
            idList.forEachIndexed { i, id ->
                val url = "https://api.spacexdata.com/v4/launches/$id"
                val request = Request.Builder()
                    .url(url)
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    override fun onResponse(call: Call, response: Response) {
                        jsonArray.put(response.body()!!.string())
                        if ((i + 1) == idList.size) {
                            LaunchesRequests(null, activity, view, value).execute(jsonArray)
                        }
                    }
                })
            }
        }
    }

    fun getDateTime(s: String): String? {
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm XXX", Locale.getDefault())
            val netDate = Date(s.toLong() * 1000)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }

    fun getStringFromList(list: ArrayList<String>?): String? {
        return if (list == null){
            null
        } else {
            var string = ""
            if (list.size == 1) {
                string = list[0]
            } else if (list.size > 1) {
                string = list[0]
                for (i in list) {
                    string += ", $i"
                }
            }
            string
        }
    }

    fun getValueFromJsonObject(json: JSONObject, valueparam: String, obj: Any): Any? {
        if (obj == String) {
            return try {
                json.getString(valueparam).toString()
            } catch (e: JSONException) {
                ""
            }
        }
        if (obj == Int) {
            return try {
                json.getInt(valueparam)
            } catch (e: JSONException) {
                null
            }
        }
        if (obj == Boolean) {
            return try {
                json.getBoolean(valueparam)
            } catch (e: JSONException) {
                null
            }
        }
        if (obj == Rocket.RocketMeasurements()) {
            val rocketMeasurements = Rocket.RocketMeasurements()
            try {
                rocketMeasurements.height = json.getJSONObject("height").getDouble("meters")
            } catch (e: JSONException) {
            }

            try {
                rocketMeasurements.diameter = json.getJSONObject("diameter").getDouble("meters")
            } catch (e: JSONException) {
            }
            try {
                rocketMeasurements.mass = json.getJSONObject("mass").getInt("kg")
            } catch (e: JSONException) {
            }
            return rocketMeasurements
        }
        if (obj == Double) {
            return try {
                json.getDouble(valueparam)
            } catch (e: JSONException) {
                null
            }
        }
        if (obj == ArrayList<String>()) {
            val images = ArrayList<String>()
            val flickr = json.getJSONArray(valueparam)
            for (i in 0 until flickr.length()) {
                images.add(flickr[i].toString())
            }
            return images
        }
        if (obj == Rocket.RocketFirstStage()) {
            val rocketFirstStage = Rocket.RocketFirstStage()

            try {
                rocketFirstStage.thrust_sea_level =
                    json.getJSONObject("first_stage").getJSONObject("thrust_sea_level").getInt("kN")
            } catch (e: JSONException) {
            }
            try {
                rocketFirstStage.thrust_vacuum =
                    json.getJSONObject("first_stage").getJSONObject("thrust_vacuum").getInt("kN")
            } catch (e: JSONException) {

            }
            try {
                rocketFirstStage.reusable = json.getJSONObject("first_stage").getBoolean("reusable")
            } catch (e: JSONException) {

            }
            try {
                rocketFirstStage.engines = json.getJSONObject("first_stage").getInt("engines")
            } catch (e: JSONException) {

            }
            try {
                rocketFirstStage.fuel_amount_tons =
                    json.getJSONObject("first_stage").getDouble("fuel_amount_tons")
            } catch (e: JSONException) {

            }
            try {
                rocketFirstStage.burn_time_sec =
                    json.getJSONObject("first_stage").getInt("burn_time_sec")
            } catch (e: JSONException) {

            }
            return rocketFirstStage
        }
        if (obj == Rocket.RocketSecondStage()) {
            val rocketSecondStage = Rocket.RocketSecondStage()

            try {
                rocketSecondStage.thrust =
                    json.getJSONObject("second_stage").getJSONObject("thrust").getInt("kN")
            } catch (e: JSONException) {
            }
            try {
                rocketSecondStage.reusable =
                    json.getJSONObject("second_stage").getBoolean("reusable")
            } catch (e: JSONException) {
            }
            try {
                rocketSecondStage.engines = json.getJSONObject("second_stage").getInt("engines")
            } catch (e: JSONException) {
            }
            try {
                rocketSecondStage.fuel_amount_tons =
                    json.getJSONObject("second_stage").getDouble("fuel_amount_tons")
            } catch (e: JSONException) {
            }
            try {
                rocketSecondStage.burn_time_sec =
                    json.getJSONObject("second_stage").getInt("burn_time_sec")
            } catch (e: JSONException) {
            }

            val compositeFairing = Rocket.RocketSecondStage.Payload.CompositeFairing()
            try {
                compositeFairing.height =
                    json.getJSONObject("second_stage").getJSONObject("payloads")
                        .getJSONObject("composite_fairing").getJSONObject("height")
                        .getDouble("meters")
                compositeFairing.diameter =
                    json.getJSONObject("second_stage").getJSONObject("payloads")
                        .getJSONObject("composite_fairing").getJSONObject("diameter")
                        .getDouble("meters")
            } catch (e: JSONException) {
            }

            val payloads = Rocket.RocketSecondStage.Payload()
            try {
                payloads.compositeFairing = compositeFairing
                payloads.option_1 = json.getJSONObject("second_stage").getJSONObject("payloads")
                    .getString("option_1")
            } catch (e: JSONException) {
            }

            rocketSecondStage.payload = payloads

            return rocketSecondStage
        }
        if (obj == Rocket.Engine()) {
            val engines = Rocket.Engine()
            val isp = Rocket.Engine.ISP()
            val enginesJson = json.getJSONObject("engines")
            try {
                isp.sea_level = enginesJson.getJSONObject("isp").getInt("sea_level")
                isp.vacuum = enginesJson.getJSONObject("isp").getInt("vacuum")
            } catch (e: JSONException) {
            }
            try {
                engines.thrust_sea_level =
                    enginesJson.getJSONObject("thrust_sea_level").getDouble("kN")
                engines.thrust_vacuum = enginesJson.getJSONObject("thrust_vacuum").getDouble("kN")
            } catch (e: JSONException) {
            }
            try {
                engines.number = enginesJson.getInt("number")
                engines.type = enginesJson.getString("type")
                engines.version = enginesJson.getString("version")
                engines.layout = enginesJson.getString("layout")
                engines.engine_loss_max = enginesJson.getInt("engine_loss_max")
                engines.propellant_1 = enginesJson.getString("propellant_1")
                engines.propellant_2 = enginesJson.getString("propellant_2")
                engines.thrust_to_weight = enginesJson.getDouble("thrust_to_weight")
            } catch (e: JSONException) {
            }
            engines.isp = isp
            return engines
        }
        if (obj == Rocket.LandingLegs()) {
            val landingLegs = Rocket.LandingLegs()
            try {
                landingLegs.number = json.getJSONObject("landing_legs").getInt("number")
                landingLegs.material = json.getJSONObject("landing_legs").getString("material")
            } catch (e: JSONException) {
            }
            return landingLegs
        }
        if (obj == Launch.Links()) {
            val links = Launch.Links()
            val linksJson = json.getJSONObject("links")
            links.mission_patch_small = linksJson.getJSONObject("patch").getString("small")
            links.reddit = linksJson.getJSONObject("reddit").getString("launch")
            val flickrImages = ArrayList<String>()
            val jsonImages = linksJson.getJSONObject("flickr").getJSONArray("original")
            for (i in 0 until jsonImages.length()) {
                flickrImages.add(jsonImages[i].toString())
            }
            links.flickr_images = flickrImages
            links.youtube = linksJson.getString("webcast")
            links.news = linksJson.getString("article")
            links.wikipedia = linksJson.getString("wikipedia")
            links.spacex = linksJson.getString("presskit")
            return links
        }
//        if (obj == ArrayList<Launch.Core>()) {
//            val cores = ArrayList<String>()
//            val coresJson = json.getJSONArray(valueparam)
//            for (i in 0 until coresJson.length()) {
////                val core = Launch.Core()
//                val coreJson = JSONObject(coresJson[i].toString())
//                try {
//                    val core = coreJson.getString("core")
//                    println(core)
//                    cores.add(core)
//                } catch (e: Exception) {
//                }
//                try {
//                    core.flight = coreJson.getInt("flight")
//                } catch (e: Exception) {
//                }
//                try {
//                    core.gridfins = coreJson.getBoolean("gridfins")
//                } catch (e: Exception) {
//                }
//                try {
//                    core.legs = coreJson.getBoolean("legs")
//                } catch (e: Exception) {
//                }
//                try {
//                    core.reused = coreJson.getBoolean("reused")
//                } catch (e: Exception) {
//                }
//                try {
//                    core.landing_attempt = coreJson.getBoolean("landing_attempt")
//                } catch (e: Exception) {
//                }
//                try {
//                    core.landing_success = coreJson.getBoolean("landing_success")
//                } catch (e: Exception) {
//                }
//                try {
//                    core.landing_type = coreJson.getString("landing_type")
//                } catch (e: Exception) {
//                }
//                try {
//                    core.landpad = coreJson.getString("landpad")
//                } catch (e: Exception) {
//                }
//            }
//            return cores
//        }
        if (obj == ArrayList<Launch.Failure>()) {
            val failures = ArrayList<Launch.Failure>()
            val jsonArray = json.getJSONArray(valueparam)
            for (i in 0 until jsonArray.length()) {
                val failure = Launch.Failure()
                failure.time = JSONObject(jsonArray[i].toString()).getInt("time")
                failure.altitude = JSONObject(jsonArray[i].toString()).getInt("altitude")
                failure.reason = JSONObject(jsonArray[i].toString()).getString("reason")
                failures.add(failure)
            }
            return failures
        }
        if (obj == Dragon.Trunk()) {
            val trunk = Dragon.Trunk()
            try {
                trunk.volumeCubicMeters = json.getJSONObject("trunk_volume").getInt("cubic_meters")
            } catch (e: Exception) {
            }
            try {
                trunk.solarArray = json.getJSONObject("cargo").getInt("solar_array")
            } catch (e: Exception) {
            }
            try {
                trunk.unpressurizedCargo =
                    json.getJSONObject("cargo").getBoolean("unpressurized_cargo")
            } catch (e: Exception) {
            }
            return trunk
        }
        if (obj == Dragon.HeatShield()) {
            val heatshield = Dragon.HeatShield()
            try {
                heatshield.material = json.getString("material")
            } catch (e: Exception) {
            }
            try {
                heatshield.sizeMeters = json.getDouble("size_meters")
            } catch (e: Exception) {
            }
            try {
                heatshield.tempDegrees = json.getDouble("temp_degrees")
            } catch (e: Exception) {
            }
            try {
                heatshield.developmentPartner = json.getString("dev_partner")
            } catch (e: Exception) {
            }

            return heatshield
        }
        if (obj == ArrayList<Dragon.Thruster>()) {
            val jsonArray = json.getJSONArray(valueparam)
            val thrusters = ArrayList<Dragon.Thruster>()
            for (i in 0 until jsonArray.length()) {
                val thruster = Dragon.Thruster()
                thruster.type = JSONObject(jsonArray[i].toString()).getString("type")
                thruster.amount = JSONObject(jsonArray[i].toString()).getInt("amount")
                thruster.pods = JSONObject(jsonArray[i].toString()).getInt("pods")
                thruster.fuel_1 = JSONObject(jsonArray[i].toString()).getString("fuel_1")
                thruster.fuel_2 = JSONObject(jsonArray[i].toString()).getString("fuel_2")
                thruster.isp = JSONObject(jsonArray[i].toString()).getInt("isp")
                thruster.thrustkN =
                    JSONObject(jsonArray[i].toString()).getJSONObject("thrust").getDouble("kN")
                thrusters.add(thruster)
            }
            return thrusters
        }

        return null
    }


//    fun getBitmapFromString(url_string: String): Bitmap? {
//        return try {
//            val url = URL(url_string)
//            val connection: HttpsURLConnection =
//                url.openConnection() as HttpsURLConnection
//            connection.doInput = true
//            connection.connect()
//            val input = connection.inputStream
//            //                bitmap = Bitmap.createScaledBitmap(bitmap, 100,100, false)
//            //                val newbitmap = compressBitmap(bitmap, 1)
//
//            BitmapFactory.decodeStream(input)
//        } catch (e: IOException) {
//            Log.e("Exceptions", e.toString())
//            null
//        }
//    }

    fun openNewTabWindow(urls: String?, context: Context, view: View) {
        try {
            val uris = Uri.parse(urls)
            val intents = Intent(Intent.ACTION_VIEW, uris)
            val b = Bundle()
            b.putBoolean("new_window", true)
            intents.putExtras(b)
            context.startActivity(intents)
        } catch (e: Exception) {
            Snackbar.make(view, "Content could not be reached", Snackbar.LENGTH_SHORT).show()
        }
    }

}