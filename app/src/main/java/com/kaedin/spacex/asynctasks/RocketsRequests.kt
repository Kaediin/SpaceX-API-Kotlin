package com.kaedin.spacex.asynctasks

import android.os.AsyncTask
import android.view.View
import com.kaedin.spacex.R
import com.kaedin.spacex.fragments.RocketsFragment
import com.kaedin.spacex.models.Rocket
import com.kaedin.spacex.utils.Create
import com.kaedin.spacex.utils.ViewUtils
import org.json.JSONArray
import org.json.JSONObject

class RocketsRequests(
    private var activity: RocketsFragment?,
    private var viewUtils: ViewUtils?,
    private var view: View?
) : AsyncTask<JSONArray, String, ArrayList<Rocket>>() {

    override fun onPreExecute() {
        super.onPreExecute()
        println("onPreExecute called")
        activity?.updateProgressBar(0, "Fetching data from SpaceX", null)
    }

    override fun doInBackground(vararg params: JSONArray?): ArrayList<Rocket> {
        println("doInBackground called")
        val jsonArray = params[0]!!
        val rockets = ArrayList<Rocket>()

        for (i in 0 until jsonArray.length()) {
            // JSON vars
            val json = try {
                jsonArray.getJSONObject(i)
            } catch (e: Exception) {
                JSONObject(jsonArray[i].toString())
            }
            val rocket = Create.rocketTile(json)
            rockets.add(rocket)
        }

        return rockets
    }

    override fun onPostExecute(result: ArrayList<Rocket>?) {
        super.onPostExecute(result)
        println("On Post Execute")
        if (viewUtils != null && view != null) {
            viewUtils?.displayRocketsInView(
                view!!,
                view!!.context,
                result!!,
                R.id.rv_launchpad_rockets
            )
        } else {
            activity?.rockets = result!!
            activity?.display()
        }
    }

    override fun onProgressUpdate(vararg values: String?) {
        activity?.updateProgressBar(values[0]!!.toInt(), values[1]!!, values[2]?.toInt())
    }
}