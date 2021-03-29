package com.kaedin.api.asynctasks

import android.os.AsyncTask
import android.view.View
import com.kaedin.api.utils.Create
import com.kaedin.api.fragments.LaunchesFragment
import com.kaedin.api.models.Launch
import com.kaedin.api.utils.DataUtils
import okhttp3.Response
import org.json.JSONArray

class LaunchesRequests(private var activity: LaunchesFragment,
                       private var view: View
) : AsyncTask<Response, String, ArrayList<Launch>>() {

    override fun onPreExecute() {
        super.onPreExecute()
        println("onPreExecute called")
        activity.updateProgressBar(view, 0, "Fetching data from SpaceX", null)
    }

    override fun doInBackground(vararg params: Response?): ArrayList<Launch> {
        println("doInBackground called")
        val jsonArray = JSONArray(params[0]!!.body()!!.string())
        val launches = ArrayList<Launch>()

        for (i in 0 until jsonArray.length()) {
            // JSON vars
            val json = jsonArray.getJSONObject(i)

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
        val n_result = DataUtils.sortOldestToNewest(result!!, true)
//        Provider.allLaunches = n_result
        activity.currentLaunches = n_result
//        activity.currentLaunches = DataUtils.filterFirstLoad(filter)
        activity.display(view)
    }

    override fun onProgressUpdate(vararg values: String?) {
        activity.updateProgressBar(view, values[0]!!.toInt(), values[1]!!, values[2]?.toInt())

    }
}