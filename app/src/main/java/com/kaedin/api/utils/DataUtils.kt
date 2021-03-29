package com.kaedin.api.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.kaedin.api.R
import com.kaedin.api.models.*
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.first_stage_list_item.view.*
import kotlinx.android.synthetic.main.launch_details_template.view.*
import kotlinx.android.synthetic.main.rocket_details_template.view.*
import kotlinx.android.synthetic.main.rocket_details_template.view.rocket_name
import kotlinx.android.synthetic.main.rocket_list_item.view.*
import kotlinx.android.synthetic.main.second_stage_list_item.view.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.URL
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.net.ssl.HttpsURLConnection
import kotlin.collections.ArrayList


object DataUtils {

    var rocket_id = ""

    fun setViewLaunchDetails(view: View, launch: Launch, context: Context) {
        Handler(Looper.getMainLooper()).post {
            view.launch_details_name.text = launch.name
            view.launch_details_details.text = launch.details
            if (launch.links?.mission_patch_small != null && launch.links?.mission_patch_small != "null") {
                Glide.with(context)
                    .load(launch.links?.mission_patch_small)
                    .into(view.launch_details_mission_patch_small)
            } else {
                view.launch_details_mission_patch_small.visibility = View.GONE
            }

            if (launch.links?.flickr_images!!.isEmpty()) {
                view.launch_details_carousel.visibility = View.GONE
                view.launch_details_active.setPadding(0, 0, 0, 50)
            } else {
                val carousel: CarouselView = view.findViewById(R.id.launch_details_carousel)
                val imageListener = ImageListener { position, imageView ->
                    imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                    imageView.setBackgroundColor(context.resources.getColor(R.color.main_background))
                    Glide.with(context)
                        .load(launch.links?.flickr_images!![position])
                        .into(imageView)
                }
                carousel.setImageListener(imageListener)
                carousel.pageCount = launch.links?.flickr_images!!.size
            }
            if (launch.success != null) {
                val title = if (launch.success!!) "SUCCESS" else "FAILED"
                view.launch_details_active.text = "LAUNCH $title"
                if (launch.success!!) {
                    view.launch_details_cardview.setCardBackgroundColor(
                        context.resources.getColor(R.color.darker_green)
                    )
                }
            }
            view.launch_details_date.text = getDateTime(launch.date_unix.toString())

            view.progress_launch_details.visibility = View.GONE
            view.rel_main_launch_details.visibility = View.VISIBLE
        }
    }

    fun setViewRocketDetails(view: View, rocket: Rocket, context: Context) {
        Handler(Looper.getMainLooper()).post {
            val carousel: CarouselView = view.findViewById(R.id.carouselView)
            val imageListener = ImageListener { position, imageView ->
                Glide.with(context)
                    .load(rocket.flickr_images!![position])
                    .into(imageView)
            }
            carousel.setImageListener(imageListener)
            carousel.pageCount = rocket.flickr_images!!.size

            view.rocket_name.text = rocket.name
            val rocketActive = if (rocket.active!!) "ACTIVE" else "INACTIVE"
            view.rocket_active.text = "ROCKET $rocketActive"
            view.frame_cardview.setCardBackgroundColor(
                if (rocket.active!!) context.resources.getColor(R.color.darker_green) else context.resources.getColor(
                    R.color.darker_red
                )
            )

            view.rocket_success_rate.text = "${rocket.success_rate_pct}%"
            val cost = NumberFormat.getNumberInstance(Locale.US).format(rocket.cost_per_launch)
                .replace(",", ".")
            view.rocket_cost_per_launch.text = "$$cost"
            view.rocket_company.text = rocket.company
            view.rocket_country.text = rocket.country
            view.rocket_diameter.text = "${rocket.measurements?.diameter.toString()} meters"
            view.rocket_height.text = "${rocket.measurements?.height.toString()} meters"
            val mass = NumberFormat.getNumberInstance(Locale.US).format(rocket.measurements?.mass)
                .replace(",", ".")
            view.rocket_mass.text = "$mass kg"
            view.rocket_boosters.text = rocket.boosters.toString()
            view.rocket_stages.text = rocket.stages.toString()
            view.rocket_first_flight.text = rocket.first_flight

            view.rocket_about.text = rocket.description
            view.rocket_wiki_button.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(rocket.wikipedia_link))
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    Snackbar.make(
                        view.rocket_list_item,
                        "Unable to locate content",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            view.rocket_first_stage.setOnClickListener {
                val builder = AlertDialog.Builder(view.context)
                val inflater = LayoutInflater.from(view.context)
                val viewFirstStage = inflater.inflate(R.layout.first_stage_list_item, null)
                builder.setView(viewFirstStage)
                val dialog = builder.create()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                viewFirstStage.first_stage_thrust_sea_level.text =
                    "${rocket.rocketFirstStage?.thrust_sea_level.toString()}kN"
                viewFirstStage.first_stage_thrust_vacuum.text =
                    "${rocket.rocketFirstStage?.thrust_vacuum.toString()}kN"
                viewFirstStage.first_stage_engines.text =
                    rocket.rocketFirstStage?.engines.toString()
                viewFirstStage.first_stage_fuel_amount.text =
                    rocket.rocketFirstStage?.fuel_amount_tons.toString()
                viewFirstStage.first_stage_burn_time.text =
                    rocket.rocketFirstStage?.burn_time_sec.toString()
                try {
                    if (rocket.rocketFirstStage?.reusable!!) {
                        viewFirstStage.first_stage_reused.setImageResource(R.drawable.ic_checkmark)
                        viewFirstStage.first_stage_reused.setColorFilter(
                            view.context.resources.getColor(
                                R.color.d_green
                            ), PorterDuff.Mode.SRC_ATOP
                        )
                    } else {
                        viewFirstStage.first_stage_reused.setImageResource(R.drawable.ic_wrong)
                        viewFirstStage.first_stage_reused.setColorFilter(
                            view.context.resources.getColor(
                                R.color.d_red
                            ), PorterDuff.Mode.SRC_ATOP
                        )
                    }
                } catch (e: Exception) {
                }

                dialog.show()
            }
            view.rocket_second_stage.setOnClickListener {
                val builder = AlertDialog.Builder(view.context)
                val inflater = LayoutInflater.from(view.context)
                val viewSecondStage = inflater.inflate(R.layout.second_stage_list_item, null)
                builder.setView(viewSecondStage)
                val dialog = builder.create()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                viewSecondStage.second_stage_thrust_vacuum.text =
                    "${rocket.rocketSecondStage?.thrust.toString()}kN"
                viewSecondStage.second_stage_engines.text =
                    rocket.rocketSecondStage?.engines.toString()
                viewSecondStage.second_stage_fuel_amount.text =
                    rocket.rocketSecondStage?.fuel_amount_tons.toString()
                viewSecondStage.second_stage_burn_time.text =
                    rocket.rocketSecondStage?.burn_time_sec.toString()

                viewSecondStage.second_stage_composite_fairing_height.text =
                    "${rocket.rocketSecondStage?.payloads?.compositeFairing?.height.toString()} meters"
                viewSecondStage.second_stage_composite_fairing_diameter.text =
                    "${rocket.rocketSecondStage?.payloads?.compositeFairing?.diameter.toString()} meters"
                viewSecondStage.second_stage_paylods_option_1.text =
                    rocket.rocketSecondStage?.payloads?.option_1.toString()

                try {
                    if (rocket.rocketSecondStage?.reusable!!) {
                        viewSecondStage.second_stage_reused.setImageResource(R.drawable.ic_checkmark)
                        viewSecondStage.second_stage_reused.setColorFilter(
                            view.context.resources.getColor(
                                R.color.d_green
                            ), PorterDuff.Mode.SRC_ATOP
                        )
                    } else {
                        viewSecondStage.second_stage_reused.setImageResource(R.drawable.ic_wrong)
                        viewSecondStage.second_stage_reused.setColorFilter(
                            view.context.resources.getColor(
                                R.color.d_red
                            ), PorterDuff.Mode.SRC_ATOP
                        )
                    }
                } catch (e: Exception) {
                }

                dialog.show()
            }

            view.engines_number.text = rocket.engines?.number.toString()
            view.engines_thrust_sea_level.text = rocket.engines?.thrust_sea_level.toString()
            view.engines_thrust_vacuum.text = rocket.engines?.thrust_vacuum.toString()
            view.engines_type.text = rocket.engines?.type.toString()
            view.engines_version.text = rocket.engines?.version.toString()
            view.engines_engine_loss_max.text = rocket.engines?.engine_loss_max.toString()
            view.engines_propellant_1.text = rocket.engines?.propellant_1.toString()
            view.engines_propellant_2.text = rocket.engines?.propellant_2.toString()
            view.engines_thrust_to_weight.text = rocket.engines?.thrust_to_weight.toString()
            view.engines_layout.text = rocket.engines?.layout.toString()
            view.rocket_landing_legs_number.text = rocket.landingLegs?.number.toString()
            view.rocket_landing_legs_material.text = rocket.landingLegs?.material.toString()

            view.progress_rocket.visibility = View.GONE
            view.rel_rocket_template.visibility = View.VISIBLE
        }
    }

    fun sortOldestToNewest(launches : ArrayList<Launch>, reverse : Boolean) : ArrayList<Launch>{
        launches.sortWith(Comparator { o1, o2 ->
            o1.flight_number!!.compareTo(o2.flight_number!!)
        })
        if (reverse){
            launches.reverse()
        }
        return launches
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

    fun getStringFromJSONArray(jsonArray: JSONArray): String {
        var string = ""
        if (jsonArray.length() == 1) {
            string = jsonArray.getString(0)
        }
        if (jsonArray.length() > 1) {
            string = jsonArray.getString(0)
            for (i in 1 until jsonArray.length()) {
                string += ", ${jsonArray.getString(i)}"
            }
        }
        return string
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
        if (obj == RocketMeasurements()) {
            val rocketMeasurements = RocketMeasurements()
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
        if (obj == RocketFirstStage()) {
            val rocketFirstStage = RocketFirstStage()

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
        if (obj == RocketSecondStage()) {
            val rocketSecondStage = RocketSecondStage()

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

            val compositeFairing = CompositeFairing()
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

            val payloads = Payloads()
            try {
                payloads.compositeFairing = compositeFairing
                payloads.option_1 = json.getJSONObject("second_stage").getJSONObject("payloads")
                    .getString("option_1")
            } catch (e: JSONException) {
            }

            rocketSecondStage.payloads = payloads

            return rocketSecondStage
        }
        if (obj == Engines()) {
            val engines = Engines()
            val isp = ISP()
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
        if (obj == LandingLegs()) {
            val landingLegs = LandingLegs()
            try {
                landingLegs.number = json.getJSONObject("landing_legs").getInt("number")
                landingLegs.material = json.getJSONObject("landing_legs").getString("material")
            } catch (e: JSONException) {
            }
            return landingLegs
        }
        if (obj == Links()) {
            val links = Links()
            val linksJson = json.getJSONObject("links")
            links.mission_patch_small = linksJson.getJSONObject("patch").getString("small")
            links.reddit = linksJson.getJSONObject("reddit").getString("launch")
            val flickrImages = ArrayList<String>()
            val jsonImages = linksJson.getJSONObject("flickr").getJSONArray("original")
            for (i in 0 until jsonImages.length()) {
                flickrImages.add(jsonImages[i].toString())
            }
            links.flickr_images = flickrImages
            links.youtube = linksJson.getString("youtube_id")
            links.news = linksJson.getString("article")
            links.wikipedia = linksJson.getString("wikipedia")
            links.spacex = linksJson.getString("presskit")
            return links
        }
        if (obj == Core()) {
            val cores = ArrayList<Core>()
            val coresJson = json.getJSONArray(valueparam)
            for (i in 0 until coresJson.length()) {
                val core = Core()
                val coreJson = JSONObject(coresJson[i].toString())
                try {
                    core.core_id = coreJson.getString("core")
                } catch (e: Exception) {
                }
                try {
                    core.flight = coreJson.getInt("flight")
                } catch (e: Exception) {
                }
                try {
                    core.gridfins = coreJson.getBoolean("gridfins")
                } catch (e: Exception) {
                }
                try {
                    core.legs = coreJson.getBoolean("legs")
                } catch (e: Exception) {
                }
                try {
                    core.reused = coreJson.getBoolean("reused")
                } catch (e: Exception) {
                }
                try {
                    core.landing_attempt = coreJson.getBoolean("landing_attempt")
                } catch (e: Exception) {
                }
                try {
                    core.landing_success = coreJson.getBoolean("landing_success")
                } catch (e: Exception) {
                }
                try {
                    core.landing_type = coreJson.getString("landing_type")
                } catch (e: Exception) {
                }
                try {
                    core.landpad = coreJson.getString("landpad")
                } catch (e: Exception) {
                }
                cores.add(core)
            }
            return cores
        }

        if (obj == ArrayList<Failure>()){
            val failures = ArrayList<Failure>()
            val jsonArray = json.getJSONArray(valueparam)
            for (i in 0 until jsonArray.length()) {
                val failure = Failure()
                failure.time = JSONObject(jsonArray[i].toString()).getInt("time")
                failure.altitude = JSONObject(jsonArray[i].toString()).getInt("altitude")
                failure.reason = JSONObject(jsonArray[i].toString()).getString("reason")
                failures.add(failure)
            }
            return failures
        }

        return null
    }


    fun getBitmapFromString(url_string: String): Bitmap? {
        return try {
            val url = URL(url_string)
            val connection: HttpsURLConnection =
                url.openConnection() as HttpsURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            //                bitmap = Bitmap.createScaledBitmap(bitmap, 100,100, false)
            //                val newbitmap = compressBitmap(bitmap, 1)

            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            Log.e("Exceptions", e.toString())
            null
        }
    }

//        private fun compressBitmap(bitmap:Bitmap, quality:Int):Bitmap{
//            val stream = ByteArrayOutputStream()
//            bitmap.compress(Bitmap.CompressFormat.PNG, quality, stream)
//
//            val byteArray = stream.toByteArray()
//
//            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//        }


}