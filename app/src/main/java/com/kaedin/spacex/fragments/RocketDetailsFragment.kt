package com.kaedin.spacex.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kaedin.spacex.R
import com.kaedin.spacex.utils.Create
import com.kaedin.spacex.utils.ViewUtils
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class RocketDetailsFragment(val rocketId : String) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.rocket_details_template, container, false)
        display(view)
        return view
    }

    fun display(view: View) {
//        val rocketId = activity!!.intent.extras!!.getString("rocket_id", "")

        val client = OkHttpClient()
        val url = "https://api.spacexdata.com/v4/rockets/$rocketId"
        println("displaying rocket fragment")
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val launchJSON = JSONObject(response.body()!!.string())
                val rocket = Create.rocket(launchJSON)
                try {
                    ViewUtils.setViewRocketDetails(view, rocket, context!!)
                } catch (e : Exception){
                    println(e.printStackTrace())
                }
            }
        })
    }
}