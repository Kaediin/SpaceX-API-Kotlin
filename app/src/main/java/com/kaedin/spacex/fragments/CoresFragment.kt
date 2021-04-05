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
import com.kaedin.spacex.adapters.AdapterMainViews
import com.kaedin.spacex.asynctasks.CoresRequests
import com.kaedin.spacex.models.Core
import com.kaedin.spacex.utils.SpaceXObject
import kotlinx.android.synthetic.main.activity_cores.*
import kotlinx.android.synthetic.main.activity_cores.view.*
import kotlinx.android.synthetic.main.activity_rockets.*
import kotlinx.android.synthetic.main.activity_rockets.view.*
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class CoresFragment : Fragment()  {

    private val client = OkHttpClient()
    private lateinit var adapter: RecyclerView.Adapter<*>
    var cores = ArrayList<Core>()
    private lateinit var mView : View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.activity_cores, container, false)
        mView = view
        run()
        return mView
    }

    private fun run() {
        val url = "https://api.spacexdata.com/v4/cores"
        val request = Request.Builder()
            .url(url)
            .build()

        updateProgressBar(0, "Trying to establishing a connection with SpaceX", null)

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                progress_horizontal_cores.visibility = View.GONE
            }
            override fun onResponse(call: Call, response: Response) {
                CoresRequests(this@CoresFragment).execute(JSONArray(response.body()!!.string()))
            }
        })
    }

    fun display() {
        Handler(Looper.getMainLooper()).post {
            val recyclerView = mView.findViewById<RecyclerView>(R.id.rv_cores)
            val columnNumbers = 2
            recyclerView.layoutManager = LinearLayoutManager(context)
            adapter = AdapterMainViews(context!!, cores, SpaceXObject.CORE, R.layout.core_list_item)
            mView.text_progress_cores.visibility = View.GONE
            mView.progress_horizontal_cores.visibility = View.GONE
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged()
        }
    }

    fun updateProgressBar(progress: Int, text: String, limit: Int?) {
        Handler(Looper.getMainLooper()).post {
            mView.text_progress_cores.text = text
            mView.progress_horizontal_cores.progress = progress
            if (limit != null) {
                mView.progress_horizontal_cores.max = limit
            }
        }
    }
}