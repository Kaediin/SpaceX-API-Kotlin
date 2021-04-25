package com.kaedin.spacex.fragments

//import com.kaedin.spacex.adapters.AdapterLaunches
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.spacex.R
import com.kaedin.spacex.adapters.AdapterMainViews
import com.kaedin.spacex.asynctasks.LaunchesRequests
import com.kaedin.spacex.models.Launch
import com.kaedin.spacex.utils.SpaceXObject
import kotlinx.android.synthetic.main.activity_launches.*
import kotlinx.android.synthetic.main.activity_launches.view.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import org.json.JSONArray
import java.io.IOException


class LaunchesFragment : Fragment(), SharedPreferences.OnSharedPreferenceChangeListener {

    private val client = OkHttpClient()
    private lateinit var adapter: AdapterMainViews
    var allLaunches = ArrayList<Launch>()
    var currentLaunches = ArrayList<Launch>()
    private lateinit var sharedPrefs : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_launches, container, false)
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPrefs.registerOnSharedPreferenceChangeListener(this)
        run(view)
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun filterLaunches(showUpcoming : Boolean){
        adapter.updateAdapterLaunches(allLaunches.filter { it.upcoming == false && !showUpcoming || showUpcoming } as ArrayList<Launch>)
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
                LaunchesRequests(this@LaunchesFragment, null, view, 3).execute(JSONArray(response.body()!!.string()))
            }
        })

    }

    fun display(view: View) {
        Handler(Looper.getMainLooper()).post {
            val recyclerView = view.findViewById<RecyclerView>(R.id.rv_launches)
            recyclerView.layoutManager = LinearLayoutManager(context)
            adapter = AdapterMainViews(requireContext(), currentLaunches, SpaceXObject.LAUNCH, R.layout.launch_list_item)
            recyclerView.adapter = adapter
            view.text_progress2.visibility = View.GONE
            view.progress_horizontal2.visibility = View.GONE
            filterLaunches(sharedPrefs.getBoolean(getString(R.string.prefs_show_upcoming), false))
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

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (sharedPreferences != null) {
            when (key) {
                getString(R.string.prefs_show_upcoming) -> {
                    filterLaunches(sharedPreferences.getBoolean(key, false))
                }
            }
        }
    }
}