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


class RocketDetailsFragment : Fragment() {

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
        val rocketId = try {
            activity!!.intent.extras!!.getString("rocket_id", "")
        } catch (e : NullPointerException){
            DataUtils.rocket_id
        }

        println("Rocket id: ${rocketId}")

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
                    DataUtils.setViewRocketDetails(view, rocket, context!!)
                } catch (e : java.lang.NullPointerException){
                    println("Nullpointer: ${e.printStackTrace()}")
                }
            }
        })
    }
}