package com.kaedin.api.adapters

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.api.googlemaps.MapViewHolder
import com.kaedin.api.googlemaps.MapViewListItemView
import com.kaedin.api.models.Landpad
import com.kaedin.api.models.Launchpad
import com.kaedin.api.utils.LandLaunchPad
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


class AdapterLandLaunchpads(
    private val context: Context,
    private val landLaunchdPads: ArrayList<*>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val mapViewListItemView = MapViewListItemView(context)
        mapViewListItemView.mapViewOnCreate(null)
        return MapViewHolder(mapViewListItemView)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val mapViewHolder = holder as MapViewHolder
        val data = ArrayList<Any>()
        try {
            val landpad = landLaunchdPads[position] as Landpad
            data.add(landpad.name)
            data.add(landpad.fullName)
            data.add(landpad.locality!!)
            data.add(landpad.region!!)
            data.add(landpad.latitude.toString())
            data.add(landpad.longitude.toString())
            data.add(landpad.id)
            data.add(LandLaunchPad.LANDPAD)
        } catch (e : Exception){
            val launchpad = landLaunchdPads[position] as Launchpad
            data.add(launchpad.name)
            data.add(launchpad.fullName)
            data.add(launchpad.locality!!)
            data.add(launchpad.region!!)
            data.add(launchpad.latitude.toString())
            data.add(launchpad.longitude.toString())
            data.add(launchpad.id)
            data.add(LandLaunchPad.LAUNCHPAD)
        }
        mapViewHolder.mapViewListItemViewOnResume(data)
    }

    override fun getItemCount() = landLaunchdPads.size
}