package com.kaedin.spacex.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.spacex.R
import com.kaedin.spacex.adapters.AdapterRockets
import com.kaedin.spacex.asynctasks.RocketsRequests
import com.kaedin.spacex.models.Rocket
import kotlinx.android.synthetic.main.activity_launches.*
import kotlinx.android.synthetic.main.activity_launches.view.*
import kotlinx.android.synthetic.main.activity_rockets.*
import kotlinx.android.synthetic.main.activity_rockets.view.*
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class RocketsFragment : Fragment() {

    private val client = OkHttpClient()
    private lateinit var adapter: RecyclerView.Adapter<*>
    var rockets = ArrayList<Rocket>()
    private lateinit var mView : View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.activity_rockets, container, false)
        run()
        mView = view
        return mView
    }

    private fun run() {
        val url = "https://api.spacexdata.com/v4/rockets"
        val request = Request.Builder()
            .url(url)
            .build()

        updateProgressBar(0, "Trying to establishing a connection with SpaceX", null)

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                progress_horizontal_rockets.visibility = View.GONE
            }
            override fun onResponse(call: Call, response: Response) {
                RocketsRequests(this@RocketsFragment, null, null).execute(JSONArray(response.body()!!.string()))
            }
        })
    }

    fun display() {
        Handler(Looper.getMainLooper()).post {
            val recyclerView = mView.findViewById<RecyclerView>(R.id.rv_rockets)
            val columnNumbers = 2
            recyclerView.layoutManager = LinearLayoutManager(context)
            adapter = AdapterRockets(context!!, rockets)
            mView.text_progress_rockets.visibility = View.GONE
            mView.progress_horizontal_rockets.visibility = View.GONE
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    fun updateProgressBar(progress: Int, text: String, limit: Int?) {
        Handler(Looper.getMainLooper()).post {
            mView.text_progress_rockets.text = text
            mView.progress_horizontal_rockets.progress = progress
            if (limit != null) {
                mView.progress_horizontal_rockets.max = limit
            }
        }
    }
}