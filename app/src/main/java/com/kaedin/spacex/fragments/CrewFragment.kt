package com.kaedin.spacex.fragments

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
import com.kaedin.spacex.R
import com.kaedin.spacex.adapters.AdapterCrew
import com.kaedin.spacex.asynctasks.CrewRequests
import com.kaedin.spacex.models.Crew
import kotlinx.android.synthetic.main.activity_crew.*
import kotlinx.android.synthetic.main.activity_crew.view.*
import kotlinx.android.synthetic.main.activity_launches.*
import kotlinx.android.synthetic.main.activity_launches.view.*
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class CrewFragment : Fragment() {

    private val client = OkHttpClient()
    private lateinit var adapter: RecyclerView.Adapter<*>
    var crews = ArrayList<Crew>()
    private lateinit var mView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.activity_crew, container, false)
        run()
        mView = view
        return mView
    }

    private fun run() {
        val url = "https://api.spacexdata.com/v4/crew"
        val request = Request.Builder()
            .url(url)
            .build()

        updateProgressBar(0, "Trying to establishing a connection with SpaceX", null)

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                progress_horizontal_crews.visibility = View.GONE
            }

            override fun onResponse(call: Call, response: Response) {
                CrewRequests(this@CrewFragment, null, null).execute(
                    JSONArray(
                        response.body()!!.string()
                    )
                )
            }
        })
    }

    fun display() {
        Handler(Looper.getMainLooper()).post {
            val recyclerView = mView.findViewById<RecyclerView>(R.id.rv_crews)
            val columnNumbers = 2
            recyclerView.layoutManager = GridLayoutManager(context, columnNumbers)
            adapter = AdapterCrew(context!!, crews)
            mView.text_progress_crews.visibility = View.GONE
            mView.progress_horizontal_crews.visibility = View.GONE
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    fun updateProgressBar(progress: Int, text: String, limit: Int?) {
        Handler(Looper.getMainLooper()).post {
            mView.text_progress_crews.text = text
            mView.progress_horizontal_crews.progress = progress
            if (limit != null) {
                mView.progress_horizontal_crews.max = limit
            }
        }
    }
}