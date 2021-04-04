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


class LandpadDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.landpad_details_template, container, false)
        display(view)
        return view
    }

    fun display(view: View) {
        val landpadId = activity!!.intent.extras!!.getString("landpad_id", "")

        val client = OkHttpClient()
        val url = "https://api.spacexdata.com/v4/landpads/$landpadId"
        println("displaying landpad fragment")
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val landpadJSON = JSONObject(response.body()!!.string())
                val landpad = Create.landpad(landpadJSON)
                try {
                    ViewUtils.setViewLandpadDetails(view, landpad, context!!)
                } catch (e : Exception){
                    println(e.printStackTrace())
                }
            }
        })
    }
}