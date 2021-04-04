package com.kaedin.spacex.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kaedin.spacex.R
import com.kaedin.spacex.models.Ship
import com.kaedin.spacex.utils.Create
import com.kaedin.spacex.utils.ViewUtils
import kotlinx.android.synthetic.main.ship_details_template.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class ShipDetailActivity : AppCompatActivity(), OnMapReadyCallback{


    private lateinit var mShip: Ship

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.ship_details_template)

        val shipId = intent.extras?.getString("ship_id")
        val client = OkHttpClient()
        val url = "https://api.spacexdata.com/v4/ships/$shipId"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val shipsJSON = JSONObject(response.body()!!.string())
                val ship = Create.ship(shipsJSON)
                mShip = ship
                val rootView = window.decorView.findViewById<RelativeLayout>(R.id.ship_template)
                ViewUtils.setViewShipDetails(rootView!!,  ship, this@ShipDetailActivity)
                if (mShip.latitude != null && mShip.longitude != null) {
                    initGoogleMaps()
                } else {
                    hideGoogleMaps()
                }
            }
        })

    }

    fun hideGoogleMaps(){
        this.runOnUiThread {
            frame_ship_map.visibility = View.GONE
        }
    }

    fun initGoogleMaps(){
        this.runOnUiThread {
            val mapFragment = supportFragmentManager.findFragmentById(R.id.ship_details_map) as? SupportMapFragment
            mapFragment?.getMapAsync(this)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val location = LatLng(mShip.latitude!!, mShip.longitude!!)
        googleMap?.apply {
            addMarker(MarkerOptions().position(location))
            animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10.0f))
            setOnMapClickListener {
                val gmmIntentUri: Uri = Uri.parse("geo:${mShip.latitude!!},${mShip.longitude!!}?q=")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
        }
    }

}