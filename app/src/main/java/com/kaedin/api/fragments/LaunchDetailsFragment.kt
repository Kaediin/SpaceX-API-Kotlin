package com.kaedin.api.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kaedin.api.R
import com.kaedin.api.utils.Create
import com.kaedin.api.utils.DataUtils
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class LaunchDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.launch_details_template, container, false)
        display(view)
        return view
    }

    private fun display(view: View) {
        val id = activity!!.intent.extras!!.getString("launch", "")

        val client = OkHttpClient()
        val url = "https://api.spacexdata.com/v4/launches/$id"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {

                val launchJSON = JSONObject(response.body()!!.string())
                val launch = Create.launchFull(launchJSON)

                /*println(launch.id)
                println(launch.name)
                println(launch.flight_number)
                println(launch.rocket_id)
                println(launch.upcoming)
                println(launch.date_unix)
                println(launch.details)
                println(launch.links)
                println(launch.fairings)
                println(launch.tbd)
                println(launch.net)
                println(launch.window)
                println(launch.success)
                println(launch.crew_ids)
                println(launch.ship_ids)
                println(launch.capsule_ids)
                println(launch.payload_ids)
                println(launch.launchpad_id)
                println(launch.auto_update)
                println(launch.launch_library_id)
                println(launch.failures)
                println(launch.cores)*/

                DataUtils.setViewLaunchDetails(view, launch, context!!)

            }
        })
    }
}