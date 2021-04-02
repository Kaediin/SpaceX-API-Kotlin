package com.kaedin.api.asynctasks

import android.os.AsyncTask
import android.view.View
import com.kaedin.api.fragments.RocketsFragment
import com.kaedin.api.models.Rocket
import com.kaedin.api.utils.Create
import com.kaedin.api.utils.ViewUtils
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

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
            viewUtils?.displayRocketsLaunchpad(view!!, view!!.context, result!!)
        } else {
            activity?.rockets = result!!
            activity?.display()
        }
    }

    override fun onProgressUpdate(vararg values: String?) {
        activity?.updateProgressBar(values[0]!!.toInt(), values[1]!!, values[2]?.toInt())
    }
}