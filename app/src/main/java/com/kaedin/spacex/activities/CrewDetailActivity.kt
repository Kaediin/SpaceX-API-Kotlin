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


class CrewDetailActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.crew_details_template)

        val crewId = intent.extras?.getString("crew_id")
        val client = OkHttpClient()
        val url = "https://api.spacexdata.com/v4/crew/$crewId"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val crewJson = JSONObject(response.body()!!.string())
                val crew = Create.crew(crewJson)

                val rootView = window.decorView.findViewById<RelativeLayout>(R.id.crew_template)
                ViewUtils.setViewCrewDetails(rootView!!, crew, this@CrewDetailActivity)
            }
        })

    }

}