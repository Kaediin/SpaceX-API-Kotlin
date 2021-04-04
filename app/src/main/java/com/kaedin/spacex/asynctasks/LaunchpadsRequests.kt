package com.kaedin.spacex.asynctasks

import android.os.AsyncTask
import com.kaedin.spacex.fragments.LaunchpadsFragment
import com.kaedin.spacex.models.Launchpad
import com.kaedin.spacex.utils.Create
import okhttp3.Response
import org.json.JSONArray

class LaunchpadsRequests(private var activity: LaunchpadsFragment) : AsyncTask<Response, String, ArrayList<Launchpad>>() {

    override fun onPreExecute() {
        super.onPreExecute()
        println("onPreExecute called")
        activity.updateProgressBar(0, "Fetching data from SpaceX", null)
    }

    override fun doInBackground(vararg params: Response?): ArrayList<Launchpad> {
        println("doInBackground called")
        val jsonArray = JSONArray(params[0]!!.body()!!.string())
        val launchpads = ArrayList<Launchpad>()

        for (i in 0 until jsonArray.length()) {
            // JSON vars
            val launchpadsJSON = jsonArray.getJSONObject(i)
            val launchpad = Create.launchpadTile(launchpadsJSON)
            launchpads.add(launchpad)
        }

        return launchpads
    }

    override fun onPostExecute(result: ArrayList<Launchpad>?) {
        super.onPostExecute(result)
        println("On Post Execute")
        activity.launchpads = result!!
        activity.display()
    }

    override fun onProgressUpdate(vararg values: String?) {
        activity.updateProgressBar(values[0]!!.toInt(), values[1]!!, values[2]?.toInt())
    }
}