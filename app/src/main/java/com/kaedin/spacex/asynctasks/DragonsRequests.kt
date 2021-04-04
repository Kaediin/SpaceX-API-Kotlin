package com.kaedin.spacex.asynctasks

import android.os.AsyncTask
import com.kaedin.spacex.fragments.DragonsFragment
import com.kaedin.spacex.models.Dragon
import com.kaedin.spacex.utils.Create
import okhttp3.Response
import org.json.JSONArray

class DragonsRequests(private var activity: DragonsFragment) : AsyncTask<Response, String, ArrayList<Dragon>>() {

    override fun onPreExecute() {
        super.onPreExecute()
        println("onPreExecute called")
        activity.updateProgressBar(0, "Fetching data from SpaceX", null)
    }

    override fun doInBackground(vararg params: Response?): ArrayList<Dragon> {
        println("doInBackground called")
        val jsonArray = JSONArray(params[0]!!.body()!!.string())
        val dragons = ArrayList<Dragon>()

        for (i in 0 until jsonArray.length()) {
            // JSON vars
            val dragonJSON = jsonArray.getJSONObject(i)
            val dragon = Create.dragonTile(dragonJSON)
            dragons.add(dragon)
        }

        return dragons
    }

    override fun onPostExecute(result: ArrayList<Dragon>?) {
        super.onPostExecute(result)
        println("On Post Execute")
        activity.dragons = result!!
        activity.display()
    }

    override fun onProgressUpdate(vararg values: String?) {
        activity.updateProgressBar(values[0]!!.toInt(), values[1]!!, values[2]?.toInt())
    }
}