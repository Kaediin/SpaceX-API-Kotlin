package com.kaedin.api.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.api.R
import com.kaedin.api.adapters.AdapterLaunches
import com.kaedin.api.asynctasks.LaunchesRequests
import com.kaedin.api.models.Launch
import kotlinx.android.synthetic.main.activity_launches.*
import kotlinx.android.synthetic.main.activity_launches.view.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONArray
import java.io.IOException


class LaunchesFragment : Fragment() {

    private val client = OkHttpClient()
    private lateinit var adapter: RecyclerView.Adapter<*>
    var currentLaunches = ArrayList<Launch>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_launches, container, false)
        run(view)
        return view
    }

    private fun run(view: View) {
        val url = "https://api.spacexdata.com/v4/launches"
        val request = Request.Builder()
            .url(url)
            .build()

        updateProgressBar(view, 0, "Trying to establishing a connection with SpaceX", null)

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                progress_horizontal2.visibility = View.GONE
            }
            override fun onResponse(call: Call, response: Response) {
                LaunchesRequests(this@LaunchesFragment, null, view, null).execute(JSONArray(response.body()!!.string()))
            }
        })

    }

    fun display(view: View) {
        Handler(Looper.getMainLooper()).post {
            val recyclerView = view.findViewById<RecyclerView>(R.id.rv_launches)
            recyclerView.layoutManager = LinearLayoutManager(context!!)
            adapter = AdapterLaunches(context!!, currentLaunches)
            recyclerView.adapter = adapter
            view.text_progress2.visibility = View.GONE
            view.progress_horizontal2.visibility = View.GONE
//            recyclerView.addItemDecoration(SimpleDividerItemDecoration(context!!))
            adapter.notifyDataSetChanged()
        }
    }

    fun updateProgressBar(view: View, progress: Int, text: String, limit: Int?) {
        Handler(Looper.getMainLooper()).post {
            view.text_progress2.text = text
            view.progress_horizontal2.progress = progress
            if (limit != null) {
                view.progress_horizontal2.max = limit
            }
        }
    }
}