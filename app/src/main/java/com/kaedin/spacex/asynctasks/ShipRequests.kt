package com.kaedin.spacex.asynctasks

import android.os.AsyncTask
import com.kaedin.spacex.utils.Create
import com.kaedin.spacex.fragments.ShipsFragment
import com.kaedin.spacex.models.Ship
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class ShipRequests(
    private var activityShips: ShipsFragment?,
    ) : AsyncTask<JSONArray, String, ArrayList<Ship>>() {

    override fun onPreExecute() {
        super.onPreExecute()
        println("onPreExecute called")
        activityShips?.updateProgressBar(0, "Fetching data from SpaceX", null)
    }

    override fun doInBackground(vararg params: JSONArray?): ArrayList<Ship> {
        println("doInBackground called")
        val jsonArray = params[0]!!
        val ships = ArrayList<Ship>()

        for (i in 0 until jsonArray.length()) {
            // JSON vars
            val json = try {
                jsonArray.getJSONObject(i)
            } catch (e: Exception) {
                JSONObject(jsonArray[i].toString())
            }

            val ship = Create.shipTile(json)
            publishProgress(
                ships.size.toString(),
                "Gathering info for flight: $i/${jsonArray.length()}",
                jsonArray.length().toString()
            )
            ships.add(ship)
        }

        return ships.reversed() as ArrayList<Ship>
    }

    override fun onPostExecute(result: ArrayList<Ship>?) {
        super.onPostExecute(result)
        println("On Post Execute")
        activityShips?.ships = result!!
        activityShips?.display()
    }

    override fun onProgressUpdate(vararg values: String?) {
        activityShips?.updateProgressBar(
            values[0]!!.toInt(),
            values[1]!!,
            values[2]?.toInt()
        )
    }
}