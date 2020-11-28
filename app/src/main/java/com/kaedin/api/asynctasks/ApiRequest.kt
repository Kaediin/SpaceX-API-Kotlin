package com.kaedin.api.asynctasks

import android.os.AsyncTask
import com.kaedin.api.utils.Create
import com.kaedin.api.utils.DataUtils
import com.kaedin.api.MainActivity
import com.kaedin.api.utils.Provider
import com.kaedin.api.models.Launch
import okhttp3.Response
import org.json.JSONArray

class ApiRequest(private var activity: MainActivity,
                 private var filter: com.kaedin.api.models.Filter
) : AsyncTask<Response, String, ArrayList<Launch>>() {

    override fun onPreExecute() {
        super.onPreExecute()
        println("onPreExecute called")
        activity.updateProgressBar(0, "Fetching data from SpaceX", null)
    }

    override fun doInBackground(vararg params: Response?): ArrayList<Launch> {
        println("doInBackground called")
        val jsonArray = JSONArray(params[0]!!.body()!!.string())
        val launches = ArrayList<Launch>()

        for (i in 0 until jsonArray.length()) {
            // JSON vars
            val launchJSON = jsonArray.getJSONObject(i)

            val launch = Create.launchTile(launchJSON)
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
        Provider.allLaunches = result!!
        activity.currentLaunches = result
        activity.currentLaunches = DataUtils.filterFirstLoad(filter)
        activity.display()
    }

    override fun onProgressUpdate(vararg values: String?) {
        activity.updateProgressBar(values[0]!!.toInt(), values[1]!!, values[2]?.toInt())

    }
}