package com.kaedin.spacex.asynctasks

import android.os.AsyncTask
import com.kaedin.spacex.utils.Create
import com.kaedin.spacex.fragments.PayloadFragment
import com.kaedin.spacex.models.Payload
import com.kaedin.spacex.utils.ViewUtils
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class PayloadRequests(
    private var activityPaylods: PayloadFragment?,
    ) : AsyncTask<JSONArray, String, ArrayList<Payload>>() {

    override fun onPreExecute() {
        super.onPreExecute()
        println("onPreExecute called")
        activityPaylods?.updateProgressBar(0, "Fetching data from SpaceX", null)
    }

    override fun doInBackground(vararg params: JSONArray?): ArrayList<Payload> {
        println("doInBackground called")
        val jsonArray = params[0]!!
        val payloads = ArrayList<Payload>()

        for (i in 0 until jsonArray.length()) {
            // JSON vars
            val json = try {
                jsonArray.getJSONObject(i)
            } catch (e: Exception) {
                JSONObject(jsonArray[i].toString())
            }

            val payload = Create.payloadTile(json)
            publishProgress(
                payloads.size.toString(),
                "Gathering info for flight: $i/${jsonArray.length()}",
                jsonArray.length().toString()
            )
            payloads.add(payload)
        }

        return payloads.reversed() as ArrayList<Payload>
    }

    override fun onPostExecute(result: ArrayList<Payload>?) {
        super.onPostExecute(result)
        println("On Post Execute")
        activityPaylods?.payloads = result!!
        activityPaylods?.display()
    }

    override fun onProgressUpdate(vararg values: String?) {
        activityPaylods?.updateProgressBar(
            values[0]!!.toInt(),
            values[1]!!,
            values[2]?.toInt()
        )
    }
}