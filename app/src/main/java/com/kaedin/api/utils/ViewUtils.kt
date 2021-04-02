package com.kaedin.api.utils

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.opengl.Visibility
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.kaedin.api.activities.DragonDetailActivity
import com.kaedin.api.R
import com.kaedin.api.activities.RocketDetailActivity
import com.kaedin.api.adapters.AdapterLaunches
import com.kaedin.api.adapters.AdapterRockets
import com.kaedin.api.holders.ViewHolder
import com.kaedin.api.models.*
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.activity_rockets.view.*
import kotlinx.android.synthetic.main.dragon_details_template.view.*
import kotlinx.android.synthetic.main.dragon_heat_shield_dialog.view.*
import kotlinx.android.synthetic.main.dragon_trunk_dialog.view.*
import kotlinx.android.synthetic.main.landpad_details_template.view.*
import kotlinx.android.synthetic.main.launch_details_template.view.*
import kotlinx.android.synthetic.main.launchpad_details_template.view.*
import kotlinx.android.synthetic.main.rocket_details_template.view.*
import kotlinx.android.synthetic.main.rocket_first_stage_dialog.view.*
import kotlinx.android.synthetic.main.rocket_second_stage_dialog.view.*
import kotlinx.android.synthetic.main.wide_view_list_item.view.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

object ViewUtils {

    private const val maxLinesDetails = 9

    fun setWideViewTemplate(
        holder: ViewHolder,
        context: Context,
        images: ArrayList<String>?,
        objectId: String,
        objectName: String,
        wideViewTemplateType: WideViewTemplateType
    ) {
        Handler(Looper.getMainLooper()).post {
            val carousel: CarouselView =
                holder.relativeLayout.findViewById(R.id.wideview_carousel)

            val imageListener = ImageListener { pos, imageView ->
                Glide.with(context)
                    .load(images!![pos])
                    .into(imageView)
                imageView.setOnClickListener {
                    launchWideViewDetails(context, objectId, wideViewTemplateType)
                }
            }
            carousel.setImageListener(imageListener)
            carousel.pageCount = images!!.size
            holder.relativeLayout.wideview_name.text = objectName

            holder.relativeLayout.wide_view_list_item.setOnClickListener {
                launchWideViewDetails(context, objectId, wideViewTemplateType)
            }
        }
    }

    fun launchWideViewDetails(
        context: Context,
        someId: String,
        wideViewTemplateType: WideViewTemplateType
    ) {
        when (wideViewTemplateType) {
            WideViewTemplateType.DRAGON -> {
                val intent = Intent(context, DragonDetailActivity::class.java)
                intent.putExtra("dragon_id", someId)
                context.startActivity(intent)
            }

            WideViewTemplateType.ROCKET -> {
                val intent = Intent(context, RocketDetailActivity::class.java)
                intent.putExtra("rocket_id", someId)
                context.startActivity(intent)
            }
        }

    }

    fun setViewLaunchDetails(view: View, launch: Launch, context: Context) {
        Handler(Looper.getMainLooper()).post {
            // Set launch name
            view.launch_details_name.text = launch.name

            // Set launch details
            view.launch_details_details.text = launch.details
            view.launch_details_details.maxLines = maxLinesDetails
            if (view.launch_details_details.lineCount > 9) {
                view.launch_details_readmore.visibility = View.VISIBLE
                view.lin_launch_details_image_header.setOnClickListener {
                    if (view.launch_details_details.maxLines == maxLinesDetails) {
                        view.launch_details_details.maxLines = Integer.MAX_VALUE
                        view.launch_details_readmore.visibility = View.GONE
                    } else {
                        view.launch_details_details.maxLines = maxLinesDetails
                        view.launch_details_readmore.visibility = View.VISIBLE
                    }
                }
            }

            // Set launch mission patch (image)
            if (launch.links.mission_patch_small != null && launch.links.mission_patch_small != "null") {
                Glide.with(context)
                    .load(launch.links.mission_patch_small)
                    .into(view.launch_details_mission_patch_small)
            } else {
                view.launch_details_mission_patch_small.visibility = View.GONE
            }

            // Set launch image carousel
            if (launch.links.flickr_images!!.isEmpty()) {
                view.launch_details_carousel.visibility = View.GONE
                view.launch_details_active.setPadding(0, 0, 0, 50)
            } else {
                val carousel: CarouselView = view.findViewById(R.id.launch_details_carousel)
                val imageListener = ImageListener { position, imageView ->
                    imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                    imageView.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.main_background
                        )
                    )
                    Glide.with(context)
                        .load(launch.links.flickr_images!![position])
                        .into(imageView)
                }
                carousel.setImageListener(imageListener)
                carousel.pageCount = launch.links.flickr_images!!.size
            }

            // Set launch carousel border
            if (launch.success != null) {
                val title = if (launch.success!!) "SUCCESS" else "FAILED"
                view.launch_details_active.text = "LAUNCH $title"
                if (launch.success!!) {
                    view.launch_details_cardview.setCardBackgroundColor(
                        ContextCompat.getColor(context, R.color.darker_green)
                    )
                }
            }

            // Set launch date
            view.launch_details_date.text = DataUtils.getDateTime(launch.date_unix.toString())

            // Set links
            view.socials_news.setOnClickListener {
                DataUtils.openNewTabWindow(launch.links.news, context, view)
            }
            view.socials_wikipedia.setOnClickListener {
                DataUtils.openNewTabWindow(launch.links.wikipedia, context, view)
            }
            view.socials_reddit.setOnClickListener {
                DataUtils.openNewTabWindow(launch.links.reddit, context, view)
            }
            view.socials_spacex.setOnClickListener {
                DataUtils.openNewTabWindow(launch.links.spacex, context, view)
            }
            view.socials_youtube.setOnClickListener {
                DataUtils.openNewTabWindow(launch.links.youtube, context, view)
            }

            // Set XML visibility
            view.progress_launch_details.visibility = View.GONE
            view.rel_main_launch_details.visibility = View.VISIBLE
        }
    }

    fun setViewRocketDetails(view: View, rocket: Rocket, context: Context) {
        Handler(Looper.getMainLooper()).post {

            // Set carousel images and settings
            val carousel: CarouselView = view.findViewById(R.id.rocket_carouselView)
            val imageListener = ImageListener { position, imageView ->
                Glide.with(context)
                    .load(rocket.flickr_images!![position])
                    .into(imageView)
            }
            carousel.setImageListener(imageListener)
            carousel.pageCount = rocket.flickr_images!!.size

            // Set rocket name
            view.rocket_name.text = rocket.name

            // Set carousel border and text
            val rocketActive = if (rocket.active!!) "ACTIVE" else "INACTIVE"
            view.rocket_active.text = "ROCKET $rocketActive"
            view.rocket_frame_cardview.setCardBackgroundColor(
                if (rocket.active!!) ContextCompat.getColor(
                    context,
                    R.color.darker_green
                ) else ContextCompat.getColor(context, R.color.darker_red)
            )

            // Set rocket properties values
            view.rocket_success_rate.text = "${rocket.success_rate_pct}%"
            val cost = NumberFormat.getNumberInstance(Locale.US).format(rocket.cost_per_launch)
                .replace(",", ".")
            view.rocket_cost_per_launch.text = "$$cost"
            view.rocket_company.text = rocket.company
            view.rocket_country.text = rocket.country
            view.rocket_diameter.text = "${rocket.measurements.diameter.toString()} meters"
            view.rocket_height.text = "${rocket.measurements.height.toString()} meters"
            val mass = NumberFormat.getNumberInstance(Locale.US).format(rocket.measurements.mass)
                .replace(",", ".")
            view.rocket_mass.text = "$mass kg"
            view.rocket_boosters.text = rocket.boosters.toString()
            view.rocket_stages.text = rocket.stages.toString()
            view.rocket_first_flight.text = rocket.first_flight

            // Set rocket description
            view.rocket_about.text = rocket.description

            // Set wikipedia button and onClickListener
            view.rocket_wiki_button.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(rocket.wikipedia_link))
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    Snackbar.make(
                        view.wide_view_list_item,
                        "Unable to locate content",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            // Set rocket first stage
            view.rocket_first_stage.setOnClickListener {
                val builder = AlertDialog.Builder(view.context)
                val inflater = LayoutInflater.from(view.context)
                val viewFirstStage = inflater.inflate(R.layout.rocket_first_stage_dialog, null)
                builder.setView(viewFirstStage)
                val dialog = builder.create()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                viewFirstStage.first_stage_thrust_sea_level.text =
                    "${rocket.rocketFirstStage.thrust_sea_level.toString()}kN"
                viewFirstStage.first_stage_thrust_vacuum.text =
                    "${rocket.rocketFirstStage.thrust_vacuum.toString()}kN"
                viewFirstStage.first_stage_engines.text =
                    rocket.rocketFirstStage.engines.toString()
                viewFirstStage.first_stage_fuel_amount.text =
                    rocket.rocketFirstStage.fuel_amount_tons.toString()
                viewFirstStage.first_stage_burn_time.text =
                    rocket.rocketFirstStage.burn_time_sec.toString()
                try {
                    if (rocket.rocketFirstStage.reusable!!) {
                        viewFirstStage.first_stage_reused.setImageResource(R.drawable.ic_checkmark)
                        viewFirstStage.first_stage_reused.setColorFilter(
                            ContextCompat.getColor(
                                context, R.color.d_green
                            ), PorterDuff.Mode.SRC_ATOP
                        )
                    } else {
                        viewFirstStage.first_stage_reused.setImageResource(R.drawable.ic_wrong)
                        viewFirstStage.first_stage_reused.setColorFilter(
                            ContextCompat.getColor(
                                context, R.color.d_red
                            ), PorterDuff.Mode.SRC_ATOP
                        )
                    }
                } catch (e: Exception) {
                }

                dialog.show()
            }

            // Set rocket second stage
            view.rocket_second_stage.setOnClickListener {
                val builder = AlertDialog.Builder(view.context)
                val inflater = LayoutInflater.from(view.context)
                val viewSecondStage = inflater.inflate(R.layout.rocket_second_stage_dialog, null)
                builder.setView(viewSecondStage)
                val dialog = builder.create()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                viewSecondStage.second_stage_thrust_vacuum.text =
                    "${rocket.rocketSecondStage.thrust.toString()}kN"
                viewSecondStage.second_stage_engines.text =
                    rocket.rocketSecondStage.engines.toString()
                viewSecondStage.second_stage_fuel_amount.text =
                    rocket.rocketSecondStage.fuel_amount_tons.toString()
                viewSecondStage.second_stage_burn_time.text =
                    rocket.rocketSecondStage.burn_time_sec.toString()

                viewSecondStage.second_stage_composite_fairing_height.text =
                    "${rocket.rocketSecondStage.payload.compositeFairing.height.toString()} meters"
                viewSecondStage.second_stage_composite_fairing_diameter.text =
                    "${rocket.rocketSecondStage.payload.compositeFairing.diameter.toString()} meters"
                viewSecondStage.second_stage_paylods_option_1.text =
                    rocket.rocketSecondStage.payload.option_1.toString()

                try {
                    if (rocket.rocketSecondStage.reusable!!) {
                        viewSecondStage.second_stage_reused.setImageResource(R.drawable.ic_checkmark)
                        viewSecondStage.second_stage_reused.setColorFilter(
                            ContextCompat.getColor(
                                context, R.color.d_green
                            ), PorterDuff.Mode.SRC_ATOP
                        )
                    } else {
                        viewSecondStage.second_stage_reused.setImageResource(R.drawable.ic_wrong)
                        viewSecondStage.second_stage_reused.setColorFilter(
                            ContextCompat.getColor(
                                context, R.color.d_red
                            ), PorterDuff.Mode.SRC_ATOP
                        )
                    }
                } catch (e: Exception) {
                }

                dialog.show()
            }

            // Set rocket engine properties
            view.engines_number.text = rocket.engine.number.toString()
            view.engines_thrust_sea_level.text = rocket.engine.thrust_sea_level.toString()
            view.engines_thrust_vacuum.text = rocket.engine.thrust_vacuum.toString()
            view.engines_type.text = rocket.engine.type.toString()
            view.engines_version.text = rocket.engine.version.toString()
            view.engines_engine_loss_max.text = rocket.engine.engine_loss_max.toString()
            view.engines_propellant_1.text = rocket.engine.propellant_1.toString()
            view.engines_propellant_2.text = rocket.engine.propellant_2.toString()
            view.engines_thrust_to_weight.text = rocket.engine.thrust_to_weight.toString()
            view.engines_layout.text = rocket.engine.layout.toString()
            view.rocket_landing_legs_number.text = rocket.landingLegs.number.toString()
            view.rocket_landing_legs_material.text = rocket.landingLegs.material.toString()

            // Set XML view
            view.progress_rocket.visibility = View.GONE
            view.rel_rocket_template.visibility = View.VISIBLE
        }
    }

    fun setViewDragonDetails(view: View, dragon: Dragon, context: Context) {
        Handler(Looper.getMainLooper()).post {

            // Set carousel images and settings
            val carousel: CarouselView = view.findViewById(R.id.dragon_carouselView)
            val imageListener = ImageListener { position, imageView ->
                Glide.with(context)
                    .load(dragon.flickrImages!![position])
                    .into(imageView)
            }
            carousel.setImageListener(imageListener)
            carousel.pageCount = dragon.flickrImages!!.size

            // Set dragon name
            view.dragon_name.text = dragon.name
            view.dragon_type.text = dragon.type

            // Set carousel border and text
            val dragonActive = if (dragon.active!!) "ACTIVE" else "INACTIVE"
            view.dragon_active.text = "DRAGON $dragonActive"
            view.dragon_frame_cardview.setCardBackgroundColor(
                if (dragon.active!!) ContextCompat.getColor(
                    context,
                    R.color.darker_green
                ) else ContextCompat.getColor(context, R.color.darker_red)
            )

            // Set dragon description
            view.dragon_about.text = dragon.description

            // Set dragon properties
            view.dragon_launch_payload_mass.text = "${dragon.launchPayloadMass.toString()} kg"
            view.dragon_launch_payload_vol.text =
                "${dragon.launchPayloadVolume.toString()} cubic meters"
            view.dragon_return_payload_mass.text = "${dragon.returnPayloadMass.toString()} kg"
            view.dragon_return_payload_vol.text =
                "${dragon.returnPayloadVolume.toString()} cubic meters"
            view.dragon_height_w_trunk.text = "${dragon.height.toString()} meters"
            view.dragon_diameter.text = "${dragon.diameter.toString()} meters"
            view.dragon_first_flight.text = dragon.firstFlight.toString()
            view.dragon_crew_capacity.text = dragon.crewCapacity.toString()
            view.dragon_orbit_duration_yr.text = dragon.orbitDurationYears.toString()

            // Set wikipedia button and onClickListener
            view.dragon_wiki_button.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(dragon.wikipedia))
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    Snackbar.make(
                        view.wide_view_list_item,
                        "Unable to locate content",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            view.dragon_heat_shield_textview.setOnClickListener {
                val builder = AlertDialog.Builder(view.context)
                val inflater = LayoutInflater.from(view.context)
                val viewHeatShield = inflater.inflate(R.layout.dragon_heat_shield_dialog, null)
                builder.setView(viewHeatShield)
                val dialog = builder.create()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                viewHeatShield.dragon_heat_shield_material.text =
                    dragon.heatShield?.material.toString()
                viewHeatShield.dragon_heat_shield_size.text =
                    "${dragon.heatShield?.sizeMeters.toString()} meters"
                viewHeatShield.dragon_heat_shield_temp_degrees.text =
                    dragon.heatShield?.tempDegrees.toString()
                viewHeatShield.dragon_heat_shield_devpartner.text =
                    dragon.heatShield?.developmentPartner.toString()

                dialog.show()
            }

            view.dragon_trunk_textview.setOnClickListener {
                val builder = AlertDialog.Builder(view.context)
                val inflater = LayoutInflater.from(view.context)
                val viewTrunk = inflater.inflate(R.layout.dragon_trunk_dialog, null)
                builder.setView(viewTrunk)
                val dialog = builder.create()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                viewTrunk.dragon_trunk_volume.text = dragon.trunk?.volumeCubicMeters.toString()
                viewTrunk.dragon_trunk_solar_array.text = dragon.trunk?.solarArray.toString()
                if (dragon.trunk?.unpressurizedCargo == true) {
                    viewTrunk.dragon_trunk_temp_unpressurized_cargo.setImageResource(R.drawable.ic_checkmark)
                    viewTrunk.dragon_trunk_temp_unpressurized_cargo.setColorFilter(
                        ContextCompat.getColor(
                            context, R.color.d_green
                        ), PorterDuff.Mode.SRC_ATOP
                    )
                } else {
                    viewTrunk.dragon_trunk_temp_unpressurized_cargo.setImageResource(
                        R.drawable.ic_wrong
                    )
                    viewTrunk.dragon_trunk_temp_unpressurized_cargo.setColorFilter(
                        ContextCompat.getColor(
                            context, R.color.d_red
                        ), PorterDuff.Mode.SRC_ATOP
                    )
                }

                dialog.show()

            }

            // Set XML view
            view.progress_dragon.visibility = View.GONE
            view.rel_dragon_template.visibility = View.VISIBLE
        }
    }

    fun setViewLandpadDetails(view: View, landpad: Landpad, context: Context) {
        Handler(Looper.getMainLooper()).post {

            view.landpad_name.text = landpad.fullName
            view.landpad_type.text = landpad.type

            // Set wikipedia button and onClickListener
            view.landpad_wiki_button.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(landpad.wikipedia))
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    Snackbar.make(
                        view.wide_view_list_item,
                        "Unable to locate content",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            view.landpad_about.text = landpad.details
            view.landpad_properties_fullname.text = landpad.fullName
            view.landpad_properties_landing_attempts.text = landpad.landingAttempts.toString()
            view.landpad_properties_landing_successes.text = landpad.landingSuccesses.toString()
            view.landpad_properties_latitude.text = landpad.latitude.toString()
            view.landpad_properties_longitude.text = landpad.longitude.toString()
            view.landpad_properties_locality.text = landpad.locality
            view.landpad_properties_name.text = landpad.name
            view.landpad_properties_region.text = landpad.region
            view.landpad_properties_type.text = landpad.type

            view.landpad_about.text = landpad.details
            view.landpad_about.maxLines = maxLinesDetails
            if (view.landpad_about.lineCount > maxLinesDetails) {
                view.landpad_details_readmore.visibility = View.VISIBLE
                view.frame_landpad_about.setOnClickListener {
                    if (view.landpad_about.maxLines == maxLinesDetails) {
                        view.landpad_about.maxLines = Integer.MAX_VALUE
                        view.landpad_details_readmore.visibility = View.GONE
                    } else {
                        view.landpad_about.maxLines = maxLinesDetails
                        view.landpad_details_readmore.visibility = View.VISIBLE
                    }
                }
            }
            view.landpad_properties_status.text = landpad.status

            if (landpad.launchIds!!.isEmpty()){
                view.frame_landpad_launches.visibility = View.GONE
            } else {
                DataUtils.setRecyclerViewMissions(view, this@ViewUtils, landpad.launchIds, true)

            }


            // Set XML view
            view.progress_landpad.visibility = View.GONE
            view.rel_landpad_template.visibility = View.VISIBLE
        }
    }

    fun setViewLaunchpadDetails(view: View, launchpad: Launchpad, context: Context) {
        Handler(Looper.getMainLooper()).post {

            view.launchpad_name.text = launchpad.fullName

            // Set wikipedia button and onClickListener
            view.launchpad_wiki_button.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(launchpad.wikipedia))
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    Snackbar.make(
                        view.wide_view_list_item,
                        "Unable to locate content",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }

            view.launchpad_about.text = launchpad.details
            view.launchpad_properties_landing_attempts.text = launchpad.landingAttempts.toString()
            view.launchpad_properties_landing_successes.text = launchpad.landingSuccesses.toString()
            view.launchpad_properties_latitude.text = launchpad.latitude.toString()
            view.launchpad_properties_longitude.text = launchpad.longitude.toString()
            view.launchpad_properties_locality.text = launchpad.locality
            view.launchpad_properties_name.text = launchpad.name
            view.launchpad_properties_region.text = launchpad.region

            view.launchpad_about.text = launchpad.details
            view.launchpad_about.maxLines = maxLinesDetails
            if (view.launchpad_about.lineCount > maxLinesDetails) {
                view.launchpad_details_readmore.visibility = View.VISIBLE
                view.frame_launchpad_about.setOnClickListener {
                    if (view.launchpad_about.maxLines == maxLinesDetails) {
                        view.launchpad_about.maxLines = Integer.MAX_VALUE
                        view.launchpad_details_readmore.visibility = View.GONE
                    } else {
                        view.launchpad_about.maxLines = maxLinesDetails
                        view.launchpad_details_readmore.visibility = View.VISIBLE
                    }
                }
            }
            view.launchpad_properties_status.text = launchpad.status

            println(launchpad.rocketIds)

            if (launchpad.rocketIds!!.isEmpty()){
                view.frame_launchpad_rockets.visibility = View.GONE
            } else {
                DataUtils.setRecyclerViewRockets(view, this@ViewUtils, launchpad.rocketIds)
            }

            if (launchpad.launchIds!!.isEmpty()){
                view.frame_launchpad_launches.visibility = View.GONE
            } else {
                DataUtils.setRecyclerViewMissions(view, this@ViewUtils, launchpad.launchIds, false)
            }


            // Set XML view
            view.progress_launchpad.visibility = View.GONE
            view.rel_launchpad_template.visibility = View.VISIBLE
        }
    }

    fun displayLaunchesLandpad(view: View, context: Context, launches: ArrayList<Launch>){
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_landpad_launches)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = AdapterLaunches(context, launches)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun displayLaunchesLaunchpad(view: View, context: Context, launches: ArrayList<Launch>){
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_launchpad_launches)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = AdapterLaunches(context, launches)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    fun displayRocketsLaunchpad(view: View, context: Context, rockets: ArrayList<Rocket>){
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_launchpad_rockets)
        recyclerView.layoutManager = LinearLayoutManager(context)
        val adapter = AdapterRockets(context, rockets)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

}