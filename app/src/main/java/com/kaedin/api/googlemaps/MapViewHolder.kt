package com.kaedin.api.googlemaps

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.api.models.Landpad

class MapViewHolder(private val mMapViewListItemView: MapViewListItemView?) :
    RecyclerView.ViewHolder(
        mMapViewListItemView!!
    ) {
    fun mapViewListItemViewOnCreate(savedInstanceState: Bundle?) {
        mMapViewListItemView?.mapViewOnCreate(savedInstanceState)
    }

    fun mapViewListItemViewOnResume(data: ArrayList<*>) {
        mMapViewListItemView?.mapViewOnResume(data)
    }

    fun mapViewListItemViewOnPause() {
        mMapViewListItemView?.mapViewOnPause()
    }

    fun mapViewListItemViewOnDestroy() {
        mMapViewListItemView?.mapViewOnDestroy()
    }

    fun mapViewListItemViewOnLowMemory() {
        mMapViewListItemView?.mapViewOnLowMemory()
    }

    fun mapViewListItemViewOnSaveInstanceState(outState: Bundle?) {
        mMapViewListItemView?.mapViewOnSaveInstanceState(outState)
    }
}