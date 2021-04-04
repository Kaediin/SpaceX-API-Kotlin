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
import com.kaedin.spacex.models.Landpad
import com.kaedin.spacex.utils.Create
import com.kaedin.spacex.utils.ViewUtils
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class LandpadDetailActivity : AppCompatActivity(), OnMapReadyCallback{


    private lateinit var mLandpad: Landpad

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.landpad_details_template)

        val landpadId = intent.extras?.getString("landpad_id")
        val client = OkHttpClient()
        val url = "https://api.spacexdata.com/v4/landpads/$landpadId"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val landpadJSON = JSONObject(response.body()!!.string())
                val landpad = Create.landpad(landpadJSON)
                mLandpad = landpad

                val rootView = window.decorView.findViewById<RelativeLayout>(R.id.landpad_template)
                ViewUtils.setViewLandpadDetails(rootView!!,  landpad, this@LandpadDetailActivity)
                initGoogleMaps()
            }
        })

    }

    fun initGoogleMaps(){
        this.runOnUiThread {
            val mapFragment = supportFragmentManager.findFragmentById(R.id.landpad_details_map) as? SupportMapFragment
            mapFragment?.getMapAsync(this)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val location = LatLng(mLandpad.latitude!!, mLandpad.longitude!!)
        googleMap?.apply {
            addMarker(MarkerOptions().position(location))
            animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10.0f))
            setOnMapClickListener {
                val gmmIntentUri: Uri = Uri.parse("geo:${mLandpad.latitude!!},${mLandpad.longitude!!}?q=")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        }
    }

}