package com.kaedin.spacex.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kaedin.spacex.R
import com.kaedin.spacex.models.Launchpad
import com.kaedin.spacex.utils.Create
import com.kaedin.spacex.utils.ViewUtils
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class LaunchpadDetailsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mLaunchpad : Launchpad

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.launchpad_details_template, container, false)
        display(view)
        return view
    }

    fun display(view: View) {
        val launchpadId = activity!!.intent.extras!!.getString("launchpad_id", "")
        println("Launchpad id: $launchpadId")

        val client = OkHttpClient()
        val url = "https://api.spacexdata.com/v4/launchpads/$launchpadId"
        println("displaying landpad fragment")
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val launchpadJSON = JSONObject(response.body()!!.string())
                val launchpad = Create.launchpad(launchpadJSON)
                mLaunchpad = launchpad
                try {
                    ViewUtils.setViewLaunchpadDetails(view, launchpad, context!!)
                    initGoogleMaps()
                } catch (e : Exception){
                    println(e.printStackTrace())
                }
            }
        })
    }

    fun initGoogleMaps(){
        this.activity!!.runOnUiThread {
            val mapFragment = childFragmentManager.findFragmentById(R.id.launchpad_details_map) as? SupportMapFragment
            mapFragment?.getMapAsync(this)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val location = LatLng(mLaunchpad.latitude!!, mLaunchpad.longitude!!)
        googleMap?.apply {
            addMarker(MarkerOptions().position(location))
            animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10.0f))
            setOnMapClickListener {
                val gmmIntentUri: Uri = Uri.parse("geo:${mLaunchpad.latitude!!},${mLaunchpad.longitude!!}?q=")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        }
    }
}