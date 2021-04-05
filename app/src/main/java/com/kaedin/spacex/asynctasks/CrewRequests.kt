package com.kaedin.spacex.asynctasks

import android.os.AsyncTask
import android.view.View
import com.kaedin.spacex.fragments.CrewFragment
import com.kaedin.spacex.models.Crew
import com.kaedin.spacex.utils.Create
import com.kaedin.spacex.utils.ViewUtils
import org.json.JSONArray
import org.json.JSONObject

class CrewRequests(
    private var activity: CrewFragment?,
    private var viewUtils: ViewUtils?,
    private var view: View?
) : AsyncTask<JSONArray, String, ArrayList<Crew>>() {

    override fun onPreExecute() {
        super.onPreExecute()
        println("onPreExecute called")
        activity?.updateProgressBar(0, "Fetching data from SpaceX", null)
    }

    override fun doInBackground(vararg params: JSONArray?): ArrayList<Crew> {
        println("doInBackground called")
        val jsonArray = params[0]!!
        val crews = ArrayList<Crew>()

        for (i in 0 until jsonArray.length()) {
            // JSON vars
            val json = try {
                jsonArray.getJSONObject(i)
            } catch (e: Exception) {
                JSONObject(jsonArray[i].toString())
            }
            val crew = Create.crew(json)
            crews.add(crew)
        }

        return crews
    }

    override fun onPostExecute(result: ArrayList<Crew>?) {
        super.onPostExecute(result)
        println("On Post Execute")
        if (viewUtils != null && view != null) {
//            viewUtils?.displaycrewsLaunchpad(view!!, view!!.context, result!!)
        } else {
            activity?.crews = result!!
            activity?.display()
        }
    }

    override fun onProgressUpdate(vararg values: String?) {
        activity?.updateProgressBar(values[0]!!.toInt(), values[1]!!, values[2]?.toInt())
    }
}