package com.kaedin.spacex.asynctasks

import android.os.AsyncTask
import com.kaedin.spacex.fragments.CapsulesFragment
import com.kaedin.spacex.models.Capsule
import com.kaedin.spacex.utils.Create
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class CapsulesRequests(
    private var activityCapsules: CapsulesFragment?,
    ) : AsyncTask<JSONArray, String, ArrayList<Capsule>>() {

    override fun onPreExecute() {
        super.onPreExecute()
        println("onPreExecute called")
        activityCapsules?.updateProgressBar(0, "Fetching data from SpaceX", null)
    }

    override fun doInBackground(vararg params: JSONArray?): ArrayList<Capsule> {
        println("doInBackground called")
        val jsonArray = params[0]!!
        val capsules = ArrayList<Capsule>()

        for (i in 0 until jsonArray.length()) {
            // JSON vars
            val json = try {
                jsonArray.getJSONObject(i)
            } catch (e: Exception) {
                JSONObject(jsonArray[i].toString())
            }

            val capsule = Create.capsule(json)
            publishProgress(
                capsules.size.toString(),
                "Gathering info for flight: $i/${jsonArray.length()}",
                jsonArray.length().toString()
            )
            capsules.add(capsule)
        }

        return capsules.reversed() as ArrayList<Capsule>
    }

    override fun onPostExecute(result: ArrayList<Capsule>?) {
        super.onPostExecute(result)
        println("On Post Execute")
        activityCapsules?.capsules = result!!
        activityCapsules?.display()
    }

    override fun onProgressUpdate(vararg values: String?) {
        activityCapsules?.updateProgressBar(
            values[0]!!.toInt(),
            values[1]!!,
            values[2]?.toInt()
        )
    }
}