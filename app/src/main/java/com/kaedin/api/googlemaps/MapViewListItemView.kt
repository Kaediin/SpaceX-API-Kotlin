package com.kaedin.api.googlemaps

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kaedin.api.R
import com.kaedin.api.activities.LandpadDetailActivity
import com.kaedin.api.activities.LaunchpadDetailActivity
import com.kaedin.api.models.Landpad
import com.kaedin.api.utils.LandLaunchPad
import kotlinx.android.synthetic.main.landpads_list_item.view.*

class MapViewListItemView constructor(context: Context?, attrs: AttributeSet? = null) :
    CardView(context!!, attrs), OnMapReadyCallback {
    private var mMapView: MapView? = null
    private var mLatitude: Double = 0.0
    private var mLongitude: Double = 0.0
    private var mId: String = ""
    private var mView: View? = null
    private var mLandLandpad : LandLaunchPad? = null

    private fun setupView() {
        mView = LayoutInflater.from(context).inflate(R.layout.landpads_list_item, this)
        mView!!.setBackgroundColor(ContextCompat.getColor(context, R.color.main_background_dark))
        mMapView = mView!!.findViewById<View>(R.id.landpad_map) as MapView
    }

    fun mapViewOnCreate(savedInstanceState: Bundle?) {
        if (mMapView != null) {
            mMapView!!.onCreate(savedInstanceState)
        }
    }

    fun mapViewOnResume(data: ArrayList<*>) {
        if (mMapView != null) {
            mMapView!!.onResume()
            mLatitude = data[4].toString().toDouble()
            mLongitude = data[5].toString().toDouble()
            mId = data[6].toString()
            mLandLandpad = data[7] as LandLaunchPad
            if (data.isNotEmpty()) {
                mView!!.landpad_name.text = data[0].toString()
                mView!!.landpad_fullname.text = data[1].toString()
                mMapView!!.getMapAsync(this)
                mView!!.landpad_locality.text = data[2].toString()
                mView!!.landpad_region.text = data[3].toString()
                mView!!.landpads_list_item.setOnClickListener {
                    if (mLandLandpad == LandLaunchPad.LANDPAD) {
                        val intent = Intent(context, LandpadDetailActivity::class.java)
                        intent.putExtra("landpad_id", mId)
                        context.startActivity(intent)
                    } else if (mLandLandpad == LandLaunchPad.LAUNCHPAD){
                        val intent = Intent(context, LaunchpadDetailActivity::class.java)
                        intent.putExtra("launchpad_id", mId)
                        context.startActivity(intent)
                    }
                }
            }
        }
    }

    fun mapViewOnPause() {
        if (mMapView != null) {
            mMapView!!.onPause()
        }
    }

    fun mapViewOnDestroy() {
        if (mMapView != null) {
            mMapView!!.onDestroy()
        }
    }

    fun mapViewOnLowMemory() {
        if (mMapView != null) {
            mMapView!!.onLowMemory()
        }
    }

    fun mapViewOnSaveInstanceState(outState: Bundle?) {
        if (mMapView != null) {
            mMapView!!.onSaveInstanceState(outState)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val location = LatLng(mLatitude, mLongitude)
        googleMap.apply {
            addMarker(MarkerOptions().position(location))
            animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10.0f))
            setOnMapClickListener {
                if (mLandLandpad == LandLaunchPad.LANDPAD) {
                    val intent = Intent(context, LandpadDetailActivity::class.java)
                    intent.putExtra("landpad_id", mId)
                    context.startActivity(intent)
                } else if (mLandLandpad == LandLaunchPad.LAUNCHPAD){
                    val intent = Intent(context, LaunchpadDetailActivity::class.java)
                    intent.putExtra("launchpad_id", mId)
                    context.startActivity(intent)
                }
            }
        }
    }

    init {
        setupView()
    }
}