package com.kaedin.spacex.activities

import android.os.Bundle
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.kaedin.spacex.R
import com.kaedin.spacex.utils.Create
import com.kaedin.spacex.utils.ViewUtils
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class PayloadDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.hide()
        setContentView(R.layout.payload_details_template)

        val payloadId = intent.extras?.getString("payload_id")
        val client = OkHttpClient()
        val url = "https://api.spacexdata.com/v4/payloads/$payloadId"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val payloadJSON = JSONObject(response.body()!!.string())
                val payload = Create.payload(payloadJSON)
                val rootView = window.decorView.findViewById<RelativeLayout>(R.id.payload_template)
                ViewUtils.setViewPayloadDetails(rootView!!,  payload, this@PayloadDetailActivity)
            }
        })
    }
}