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
import com.kaedin.spacex.adapters.AdapterGoogleMaps
import com.kaedin.spacex.asynctasks.LaunchpadsRequests
import com.kaedin.spacex.models.Launchpad
import kotlinx.android.synthetic.main.activity_landpads.*
import kotlinx.android.synthetic.main.activity_landpads.view.*
import kotlinx.android.synthetic.main.activity_rockets.*
import kotlinx.android.synthetic.main.activity_rockets.view.*
import okhttp3.*
import java.io.IOException

class LaunchpadsFragment : Fragment()  {

    private val client = OkHttpClient()
    private lateinit var adapter: RecyclerView.Adapter<*>
    var launchpads = ArrayList<Launchpad>()
    private lateinit var mView : View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.activity_landpads, container, false)
        mView = view
        run()
        return mView
    }

    private fun run() {
        val url = "https://api.spacexdata.com/v4/launchpads"
        val request = Request.Builder()
            .url(url)
            .build()

        updateProgressBar(0, "Trying to establishing a connection with SpaceX", null)

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                progress_horizontal_landpads.visibility = View.GONE
            }
            override fun onResponse(call: Call, response: Response) {
                LaunchpadsRequests(this@LaunchpadsFragment).execute(response)
            }
        })
    }

    fun display() {
        Handler(Looper.getMainLooper()).post {
            val recyclerView = mView.findViewById<RecyclerView>(R.id.rv_landpads)
            val columnNumbers = 2
            recyclerView.layoutManager = LinearLayoutManager(context)
            adapter = AdapterGoogleMaps(context!!, launchpads)
            mView.text_progress_landpads.visibility = View.GONE
            mView.progress_horizontal_landpads.visibility = View.GONE
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    fun updateProgressBar(progress: Int, text: String, limit: Int?) {
        Handler(Looper.getMainLooper()).post {
            mView.text_progress_landpads.text = text
            mView.progress_horizontal_landpads.progress = progress
            if (limit != null) {
                mView.progress_horizontal_landpads.max = limit
            }
        }
    }
}