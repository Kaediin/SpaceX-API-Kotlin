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


class CapsuleDetailActivity : AppCompatActivity(){



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.capsule_details_template)

        val capsuleId = intent.extras?.getString("capsule_id")
        val client = OkHttpClient()
        val url = "https://api.spacexdata.com/v4/capsules/$capsuleId"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val capsuleJSON = JSONObject(response.body()!!.string())
                val capsule = Create.capsule(capsuleJSON)

                val rootView = window.decorView.findViewById<RelativeLayout>(R.id.capsule_template)
                ViewUtils.setViewCapsuleDetails(rootView!!,  capsule, this@CapsuleDetailActivity)
            }
        })

    }



}