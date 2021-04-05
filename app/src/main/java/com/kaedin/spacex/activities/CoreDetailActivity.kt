package com.kaedin.spacex.activities

import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.*
import com.kaedin.spacex.R
import com.kaedin.spacex.utils.Create
import com.kaedin.spacex.utils.ViewUtils
import kotlinx.android.synthetic.main.ship_details_template.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException


class CoreDetailActivity : AppCompatActivity(){



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.core_details_template)

        val coreId = intent.extras?.getString("core_id")
        val client = OkHttpClient()
        val url = "https://api.spacexdata.com/v4/cores/$coreId"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val coreJSON = JSONObject(response.body()!!.string())
                val core = Create.core(coreJSON)

                val rootView = window.decorView.findViewById<RelativeLayout>(R.id.core_template)
                ViewUtils.setViewCoreDetails(rootView!!,  core, this@CoreDetailActivity)
            }
        })

    }



}