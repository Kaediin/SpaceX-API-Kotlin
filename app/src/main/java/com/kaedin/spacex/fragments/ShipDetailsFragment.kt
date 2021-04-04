package com.kaedin.spacex.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.kaedin.spacex.R
import com.kaedin.spacex.adapters.AdapterShips
import com.kaedin.spacex.models.Ship
import com.kaedin.spacex.utils.Create
import com.kaedin.spacex.utils.ViewUtils
import kotlinx.android.synthetic.main.capsules_rv_details_template.view.*
import kotlinx.android.synthetic.main.ship_details_template.*
import kotlinx.android.synthetic.main.ships_rv_details_template.view.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ShipDetailsFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mView: View
    private lateinit var mShip: Ship
    private var mRecyclerView : RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.ships_rv_details_template, container, false)
        mView = view
        display()
        return view
    }

    fun display() {
        val shipIds = activity!!.intent.extras!!.getStringArrayList("ship_ids")
        val ships = ArrayList<Ship>()
        var counter = 0
        if (shipIds!!.isNotEmpty()) {
            for (id in shipIds.iterator()) {
                val client = OkHttpClient()
                val url = "https://api.spacexdata.com/v4/ships/$id"
                val request = Request.Builder()
                    .url(url)
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {}
                    override fun onResponse(call: Call, response: Response) {
                        try {
                            val shipJSON = JSONObject(response.body()!!.string())
                            val ship = Create.ship(shipJSON)
                            ships.add(ship)

                            println(ships.size)
                            if (counter == (shipIds.size - 1)) {
                                setViews(ships)
                            }
                            counter += 1

                        } catch (e: Exception) {
                            println(e.printStackTrace())
                        }
                    }
                })
            }
        } else {
            mView.no_data_ships.visibility = View.VISIBLE
            mView.rv_ships_details.visibility = View.GONE
            mView.progress_ships_details.visibility = View.GONE
        }
    }

    fun setViews(ships: ArrayList<Ship>) {
        Handler(Looper.getMainLooper()).post {

            if (ships.size > 1) {
                mRecyclerView = mView.findViewById(R.id.rv_ships_details)
                mRecyclerView?.layoutManager = LinearLayoutManager(mView.context)
                val adapter = AdapterShips(mView.context, ships)
                mRecyclerView?.adapter = adapter
                adapter.notifyDataSetChanged()
                mView.rel_view_ships_rv_template.setBackgroundColor(ContextCompat.getColor(mView.context, R.color.main_background_dark))
                mView.progress_ships_details.visibility = View.GONE
            } else {
                val inflater =
                    activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                mView = inflater.inflate(R.layout.ship_details_template, null)
                val rootView = view as ViewGroup
                rootView.removeAllViews()
                rootView.addView(mView)
                mShip = ships[0]
                ViewUtils.setViewShipDetails(mView, ships[0], context!!)

                if (mShip.latitude != null && mShip.longitude != null) {
                    initGoogleMaps()
                } else {
                    hideGoogleMaps()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        try {
            val fragment =
                activity?.supportFragmentManager?.findFragmentById(R.id.ship_details_map) as? SupportMapFragment
            if (fragment != null) {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.remove(fragment)
                    ?.commit()
            }
        }catch (e : Exception){

        }
    }

    fun hideGoogleMaps() {
        Handler(Looper.getMainLooper()).post {
            frame_ship_map.visibility = View.GONE
        }
    }

    fun initGoogleMaps() {
        Handler(Looper.getMainLooper()).post {
            val mapFragment =
                activity?.supportFragmentManager?.findFragmentById(R.id.ship_details_map) as? SupportMapFragment
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