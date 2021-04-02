package com.kaedin.api.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kaedin.api.R
import com.kaedin.api.utils.Create
import com.kaedin.api.utils.ViewUtils
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class LaunchpadDetailsFragment : Fragment() {

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
                try {
                    ViewUtils.setViewLaunchpadDetails(view, launchpad, context!!)
                } catch (e : Exception){
                    println(e.printStackTrace())
                }
            }
        })
    }
}