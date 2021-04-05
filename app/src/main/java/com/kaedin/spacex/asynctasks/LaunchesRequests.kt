package com.kaedin.spacex.asynctasks

import android.os.AsyncTask
import android.view.View
import com.kaedin.spacex.R
import com.kaedin.spacex.fragments.LaunchesFragment
import com.kaedin.spacex.models.Launch
import com.kaedin.spacex.utils.Create
import com.kaedin.spacex.utils.DataUtils
import com.kaedin.spacex.utils.ViewUtils
import org.json.JSONArray
import org.json.JSONObject

class LaunchesRequests(
    private var activityLaunches: LaunchesFragment?,
    private var activityViewUtils: ViewUtils?,
    private var view: View,
    private var value: Int
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

            launches.add(launch)


        }

        return launches
    }

    override fun onPostExecute(result: ArrayList<Launch>?) {
        super.onPostExecute(result)
        println("On Post Execute")

        val resultsSorted = DataUtils.sortOldestToNewest(result!!, true)
        var layout: Int? = null

        when (value) {
            1 -> layout = R.id.rv_landpad_launches
            2 -> layout = R.id.rv_launchpad_launches
            3 -> {
                activityLaunches?.currentLaunches = resultsSorted
                activityLaunches?.display(view)
            }
            4 -> layout = R.id.rv_payload_launch
            5 -> layout = R.id.rv_ship_launches
            6 -> layout = R.id.rv_capsule_launches
            7 -> layout = R.id.rv_core_launches
            8 -> layout = R.id.rv_crew_launches
        }
        if (layout != null) activityViewUtils?.displayLaunchesInView(
            view,
            view.context,
            resultsSorted,
            layout
        )
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