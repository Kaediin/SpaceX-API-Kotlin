package com.kaedin.spacex.asynctasks

import android.os.AsyncTask
import com.kaedin.spacex.fragments.CoresFragment
import com.kaedin.spacex.models.Core
import com.kaedin.spacex.utils.Create
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class CoresRequests(
    private var activityCores: CoresFragment?,
    ) : AsyncTask<JSONArray, String, ArrayList<Core>>() {

    override fun onPreExecute() {
        super.onPreExecute()
        println("onPreExecute called")
        activityCores?.updateProgressBar(0, "Fetching data from SpaceX", null)
    }

    override fun doInBackground(vararg params: JSONArray?): ArrayList<Core> {
        println("doInBackground called")
        val jsonArray = params[0]!!
        val cores = ArrayList<Core>()

        for (i in 0 until jsonArray.length()) {
            // JSON vars
            val json = try {
                jsonArray.getJSONObject(i)
            } catch (e: Exception) {
                JSONObject(jsonArray[i].toString())
            }

            val core = Create.core(json)
            publishProgress(
                cores.size.toString(),
                "Gathering info for flight: $i/${jsonArray.length()}",
                jsonArray.length().toString()
            )
            cores.add(core)
        }

        return cores.reversed() as ArrayList<Core>
    }

    override fun onPostExecute(result: ArrayList<Core>?) {
        super.onPostExecute(result)
        println("On Post Execute")
        activityCores?.cores = result!!
        activityCores?.display()
    }

    override fun onProgressUpdate(vararg values: String?) {
        activityCores?.updateProgressBar(
            values[0]!!.toInt(),
            values[1]!!,
            values[2]?.toInt()
        )
    }
}