package com.kaedin.api.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.api.R
import com.kaedin.api.adapters.AdapterRockets
import com.kaedin.api.asynctasks.RocketsRequests
import com.kaedin.api.models.Rocket
import kotlinx.android.synthetic.main.activity_launches.*
import kotlinx.android.synthetic.main.activity_launches.progress_horizontal2
import kotlinx.android.synthetic.main.activity_launches.view.*
import kotlinx.android.synthetic.main.activity_rockets.*
import kotlinx.android.synthetic.main.activity_rockets.view.*
import okhttp3.*
import java.io.IOException

class RocketsFragment : Fragment() {

    private val client = OkHttpClient()
    private lateinit var adapter: RecyclerView.Adapter<*>
    var rockets = ArrayList<Rocket>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_rockets, container, false)
        run(view)
        return view
    }

    private fun run(view: View) {
        val url = "https://api.spacexdata.com/v4/rockets"
        val request = Request.Builder()
            .url(url)
            .build()

        updateProgressBar(view, 0, "Trying to establishing a connection with SpaceX", null)

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                progress_horizontal_rockets.visibility = View.GONE
            }
            override fun onResponse(call: Call, response: Response) {
                RocketsRequests(this@RocketsFragment, view).execute(response)
            }
        })
    }

    fun display(view: View) {
        Handler(Looper.getMainLooper()).post {
            val recyclerView = view.findViewById<RecyclerView>(R.id.rv_rockets)
            val columnNumbers = 2
            recyclerView.layoutManager = LinearLayoutManager(context)
            adapter = AdapterRockets(context!!, rockets)
            view.text_progress_rockets.visibility = View.GONE
            view.progress_horizontal_rockets.visibility = View.GONE
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    fun updateProgressBar(view: View, progress: Int, text: String, limit: Int?) {
        Handler(Looper.getMainLooper()).post {
            view.text_progress_rockets.text = text
            view.progress_horizontal_rockets.progress = progress
            if (limit != null) {
                view.progress_horizontal_rockets.max = limit
            }
        }
    }
}