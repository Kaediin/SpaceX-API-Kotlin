package com.kaedin.api.asynctasks

import android.os.AsyncTask
import android.view.View
import com.kaedin.api.utils.Create
import com.kaedin.api.fragments.LaunchesFragment
import com.kaedin.api.models.Launch
import com.kaedin.api.utils.DataUtils
import com.kaedin.api.utils.ViewUtils
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class LaunchesRequests(
    private var activityLaunches: LaunchesFragment?,
    private var activityViewUtils: ViewUtils?,
    private var view: View,
    private var isLandpad: Boolean?
) : AsyncTask<JSONArray, String, ArrayList<Launch>>() {

    override fun onPreExecute() {
        super.onPreExecute()
        println("onPreExecute called")
        activityLaunches?.updateProgressBar(view, 0, "Fetching data from SpaceX", null)
    }

    override fun doInBackground(vararg params: JSONArray?): ArrayList<Launch> {
        println("doInBackground called")
        val jsonArray = params[0]!!
        val launches = ArrayList<Launch>()

        for (i in 0 until jsonArray.length()) {
            // JSON vars
            val json = try {
                jsonArray.getJSONObject(i)
            } catch (e: Exception) {
                JSONObject(jsonArray[i].toString())
            }

            val launch = Create.launchTile(json)
            publishProgress(
                launches.size.toString(),
                "Gathering info for flight: $i/${jsonArray.length()}",
                jsonArray.length().toString()
            )
            if (launch.upcoming == false) {
                launches.add(launch)
            }


        }

        return launches
    }

    override fun onPostExecute(result: ArrayList<Launch>?) {
        super.onPostExecute(result)
        println("On Post Execute")
        val resultsSorted = DataUtils.sortOldestToNewest(result!!, true)

        if (isLandpad != null) {
            if (isLandpad!!) {
                activityViewUtils?.displayLaunchesLandpad(view, view.context, resultsSorted)
            } else {
                activityViewUtils?.displayLaunchesLaunchpad(view, view.context, resultsSorted)
            }
        } else {
            activityLaunches?.currentLaunches = resultsSorted
            activityLaunches?.display(view)
        }
    }

    override fun onProgressUpdate(vararg values: String?) {
        activityLaunches?.updateProgressBar(
            view,
            values[0]!!.toInt(),
            values[1]!!,
            values[2]?.toInt()
        )
    }
}