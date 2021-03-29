package com.kaedin.api.asynctasks

import android.os.AsyncTask
import android.view.View
import com.kaedin.api.fragments.RocketsFragment
import com.kaedin.api.models.Rocket
import com.kaedin.api.utils.Create
import okhttp3.Response
import org.json.JSONArray

class RocketsRequests(private var activity: RocketsFragment,
                      private var view: View
) : AsyncTask<Response, String, ArrayList<Rocket>>() {

    override fun onPreExecute() {
        super.onPreExecute()
        println("onPreExecute called")
        activity.updateProgressBar(view, 0, "Fetching data from SpaceX", null)
    }

    override fun doInBackground(vararg params: Response?): ArrayList<Rocket> {
        println("doInBackground called")
        val jsonArray = JSONArray(params[0]!!.body()!!.string())
        val rockets = ArrayList<Rocket>()

        for (i in 0 until jsonArray.length()) {
            // JSON vars
            val rocketJSON = jsonArray.getJSONObject(i)
            val rocket = Create.rocket(rocketJSON)
            rockets.add(rocket)
        }

        return rockets
    }

    override fun onPostExecute(result: ArrayList<Rocket>?) {
        super.onPostExecute(result)
        println("On Post Execute")
        activity.rockets = result!!
        activity.display(view)
    }

    override fun onProgressUpdate(vararg values: String?) {
        activity.updateProgressBar(view, values[0]!!.toInt(), values[1]!!, values[2]?.toInt())
    }
}