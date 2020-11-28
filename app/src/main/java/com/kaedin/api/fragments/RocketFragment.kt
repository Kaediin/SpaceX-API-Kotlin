package com.kaedin.api.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.api.*
import com.kaedin.api.adapters.AdapterCores
import com.kaedin.api.adapters.AdapterPayload
import com.kaedin.api.models.*
import com.kaedin.api.utils.Create
import kotlinx.android.synthetic.main.rocket_template.view.*
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class RocketFragment : Fragment() {

    private lateinit var recyclerView_first: RecyclerView
    private lateinit var recyclerView_second: RecyclerView
    private lateinit var viewAdapter_first: RecyclerView.Adapter<*>
    private lateinit var viewAdapter_second: RecyclerView.Adapter<*>
    private lateinit var viewManager_first: RecyclerView.LayoutManager
    private lateinit var viewManager_second: RecyclerView.LayoutManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.rocket_template, container, false)
        display(view)
        return view
    }

    fun display(view: View) {
        val id = activity!!.intent.extras!!.getInt("launch", -1)

        val client = OkHttpClient()
        val url = "https://api.spacexdata.com/v3/launches"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {

                val json_array = JSONArray(response.body()!!.string())
                for (i in 0 until json_array.length()) {
                    // JSON vars
                    val launchJSON = json_array.getJSONObject(i)
                    val fl = launchJSON.getInt("flight_number")
                    if (id == fl) {

                        val rocket = Create.rocket(launchJSON)

                        setView(view, rocket)
                        break
                    }
                }
            }
        })


    }



    fun setView(view: View, rocket: Rocket) {
        (activity as DetailActivity?)?.runOnUiThread {
            view.progress_rocket.visibility = View.GONE
            view.rel_layout_rocket.visibility = View.VISIBLE

            viewManager_first = LinearLayoutManager(context)
            viewManager_second = LinearLayoutManager(context)

            viewAdapter_first = AdapterCores(this.context!!, rocket.firstStage?.cores)
            viewAdapter_second = AdapterPayload(this.context!!, rocket.secondStage?.payloads)

            recyclerView_first = view.findViewById<RecyclerView>(R.id.rv_firststage).apply {
                isNestedScrollingEnabled = false
                layoutManager = viewManager_first
                adapter = viewAdapter_first
            }

            recyclerView_second = view.findViewById<RecyclerView>(R.id.rv_secondstage).apply {
                isNestedScrollingEnabled = false
                layoutManager = viewManager_second
                adapter = viewAdapter_second
            }

            view.reused.text = rocket.fairing?.reused
            view.recoveryAttempt.text = rocket.fairing?.recovery_attempt
            view.recovered.text = rocket.fairing?.recovered
            view.ship.text = rocket.fairing?.ship
            (activity as DetailActivity?)?.onDataSetChanged()
        }
    }
}