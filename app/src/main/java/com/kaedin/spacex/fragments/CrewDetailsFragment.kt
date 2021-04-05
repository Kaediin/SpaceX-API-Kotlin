package com.kaedin.spacex.fragments

import android.content.Context
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
import com.kaedin.spacex.adapters.AdapterMainViews
//import com.kaedin.spacex.adapters.AdapterCrew
import com.kaedin.spacex.models.Crew
import com.kaedin.spacex.utils.Create
import com.kaedin.spacex.utils.SpaceXObject
import com.kaedin.spacex.utils.ViewUtils
import kotlinx.android.synthetic.main.crew_rv_details_template.view.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class CrewDetailsFragment : Fragment() {

    private lateinit var mView: View
    private var mRecyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.crew_rv_details_template, container, false)
        mView = view
        display()
        return view
    }

    fun display() {
        val crewIds = activity!!.intent.extras!!.getStringArrayList("crew_ids")
        val crews = ArrayList<Crew>()
        println("crew ids: $crewIds")
        var counter = 0
        if (crewIds!!.isNotEmpty()) {
            for (id in crewIds.iterator()) {
                val client = OkHttpClient()
                val url = "https://api.spacexdata.com/v4/crew/$id"
                val request = Request.Builder()
                    .url(url)
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {}
                    override fun onResponse(call: Call, response: Response) {
                        try {
                            val crewJSON = JSONObject(response.body()!!.string())
                            val crew = Create.crew(crewJSON)
                            crews.add(crew)

                            if (counter == (crewIds.size - 1)) {
                                setViews(crews)
                            }
                            counter += 1

                        } catch (e: Exception) {
                        }
                    }
                })
            }
        } else {
            mView.no_data_crew.visibility = View.VISIBLE
            mView.rv_crew_details.visibility = View.GONE
            mView.progress_crew_details.visibility = View.GONE
        }
    }

    fun setViews(crews: ArrayList<Crew>) {
        Handler(Looper.getMainLooper()).post {
            println(crews)
            when (crews.size) {
                1 -> {
                    val inflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    mView = inflater.inflate(R.layout.crew_details_template, null)
                    val rootView = view as ViewGroup
                    rootView.removeAllViews()
                    rootView.addView(mView)
                    ViewUtils.setViewCrewDetails(mView, crews[0], context!!)
                }
                else -> {
                    mRecyclerView = mView.findViewById(R.id.rv_crew_details)
                    mRecyclerView?.layoutManager = GridLayoutManager(mView.context, 2)
                    val adapter = AdapterMainViews(mView.context, crews, SpaceXObject.CREW, R.layout.wide_view_list_item)
                    mRecyclerView?.adapter = adapter
                    adapter.notifyDataSetChanged()
                    mView.progress_crew_details.visibility = View.GONE
                }
            }
        }
    }
}