package com.kaedin.api.asynctasks

import android.os.AsyncTask
import com.kaedin.api.fragments.LandpadsFragment
import com.kaedin.api.models.Landpad
import com.kaedin.api.utils.Create
import okhttp3.Response
import org.json.JSONArray

class LandpadsRequests(private var activity: LandpadsFragment) : AsyncTask<Response, String, ArrayList<Landpad>>() {

    override fun onPreExecute() {
        super.onPreExecute()
        println("onPreExecute called")
        activity.updateProgressBar(0, "Fetching data from SpaceX", null)
    }

    override fun doInBackground(vararg params: Response?): ArrayList<Landpad> {
        println("doInBackground called")
        val jsonArray = JSONArray(params[0]!!.body()!!.string())
        val landpads = ArrayList<Landpad>()

        for (i in 0 until jsonArray.length()) {
            // JSON vars
            val landpadsJSON = jsonArray.getJSONObject(i)
            val landpad = Create.landpadTile(landpadsJSON)
            landpads.add(landpad)
        }

        return landpads
    }

    override fun onPostExecute(result: ArrayList<Landpad>?) {
        super.onPostExecute(result)
        println("On Post Execute")
        activity.landpads = result!!
        activity.display()
    }

    override fun onProgressUpdate(vararg values: String?) {
        activity.updateProgressBar(values[0]!!.toInt(), values[1]!!, values[2]?.toInt())
    }
}