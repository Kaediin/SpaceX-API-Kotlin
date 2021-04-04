package com.kaedin.spacex.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kaedin.spacex.R
import com.kaedin.spacex.models.Launchpad
import com.kaedin.spacex.utils.Create
import com.kaedin.spacex.utils.ViewUtils
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class LaunchpadDetailActivity : AppCompatActivity(), OnMapReadyCallback{


    private lateinit var mLaunchpad: Launchpad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.launchpad_details_template)

        val launchpadId = intent.extras?.getString("launchpad_id")
        val client = OkHttpClient()
        val url = "https://api.spacexdata.com/v4/launchpads/$launchpadId"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val launchpadJSON = JSONObject(response.body()!!.string())
                val launchpad = Create.launchpad(launchpadJSON)
                mLaunchpad = launchpad

                val rootView = window.decorView.findViewById<RelativeLayout>(R.id.launchpad_template)
                ViewUtils.setViewLaunchpadDetails(rootView!!,  launchpad, this@LaunchpadDetailActivity)
                initGoogleMaps()
            }
        })

    }

    fun initGoogleMaps(){
        this.runOnUiThread {
            val mapFragment = supportFragmentManager.findFragmentById(R.id.launchpad_details_map) as? SupportMapFragment
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