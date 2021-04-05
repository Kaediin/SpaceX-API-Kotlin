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


class DragonDetailActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.dragon_details_template)

        val dragonId = intent.extras?.getString("dragon_id")
        val client = OkHttpClient()
        val url = "https://api.spacexdata.com/v4/dragons/$dragonId"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val dragonJSON = JSONObject(response.body()!!.string())
                val dragon = Create.dragon(dragonJSON)

                val rootView = window.decorView.findViewById<RelativeLayout>(R.id.dragon_template)
                ViewUtils.setViewDragonDetails(rootView!!, dragon, this@DragonDetailActivity)
            }
        })

    }

}