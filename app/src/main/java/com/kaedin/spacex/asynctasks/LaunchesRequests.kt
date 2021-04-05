package com.kaedin.spacex.asynctasks

import android.os.AsyncTask
import android.view.View
import com.kaedin.spacex.utils.Create
import com.kaedin.spacex.fragments.LaunchesFragment
import com.kaedin.spacex.models.Launch
import com.kaedin.spacex.utils.DataUtils
import com.kaedin.spacex.utils.ViewUtils
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

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
//        val resultsSorted = result!!
        val resultsSorted = DataUtils.sortOldestToNewest(result!!, true)

        when (value) {
            1 -> {
                activityViewUtils?.displayLaunchesLandpad(view, view.context, resultsSorted)
            }

            2 -> {
                activityViewUtils?.displayLaunchesLaunchpad(view, view.context, resultsSorted)
            }

            3 -> {
                activityLaunches?.currentLaunches = resultsSorted
                activityLaunches?.display(view)
            }

            4 -> {
                activityViewUtils?.displayPayloadLaunch(view, view.context, resultsSorted)
            }

            5 -> {
                activityViewUtils?.displayLaunchesShip(view, view.context, resultsSorted)
            }

            6 -> {
                activityViewUtils?.displayLaunchesCapsules(view, view.context, resultsSorted)
            }

            7 -> {
                activityViewUtils?.displayLaunchesCores(view, view.context, resultsSorted)
            }

             8 -> {
                 activityViewUtils?.displayLaunchesCrew(view, view.context, resultsSorted)
             }
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