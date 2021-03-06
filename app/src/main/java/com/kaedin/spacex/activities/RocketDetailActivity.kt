package com.kaedin.spacex.activities

import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.kaedin.spacex.R
import com.kaedin.spacex.utils.Create
import com.kaedin.spacex.utils.ViewUtils
import kotlinx.android.synthetic.main.rocket_details_template.view.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.*


class RocketDetailActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.rocket_details_template)

        val rocketId = intent.extras?.getString("rocket_id")
        val client = OkHttpClient()
        val url = "https://api.spacexdata.com/v4/rockets/$rocketId"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val launchJSON = JSONObject(response.body()!!.string())
                val rocket = Create.rocket(launchJSON)

                val rootView = window.decorView.findViewById<RelativeLayout>(R.id.rocket_template)
                ViewUtils.setViewRocketDetails(rootView!!, rocket, this@RocketDetailActivity)
            }
        })

    }

}