package com.kaedin.spacex.fragments

import android.content.Context
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
import com.kaedin.spacex.models.Core
import com.kaedin.spacex.utils.Create
import com.kaedin.spacex.utils.SpaceXObject
import com.kaedin.spacex.utils.ViewUtils
import kotlinx.android.synthetic.main.cores_rv_details_template.view.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class CoreDetailsFragment : Fragment() {

    private lateinit var mView: View
    private var mRecyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.cores_rv_details_template, container, false)
        mView = view
        display()
        return view
    }

    fun display() {
        val coreIds = activity!!.intent.extras!!.getStringArrayList("core_ids")
        val cores = ArrayList<Core>()
        println("core ids: $coreIds")
        var counter = 0
        if (coreIds!!.isNotEmpty()) {
            for (id in coreIds.iterator()) {
                val client = OkHttpClient()
                val url = "https://api.spacexdata.com/v4/cores/$id"
                val request = Request.Builder()
                    .url(url)
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {}
                    override fun onResponse(call: Call, response: Response) {
                        try {
                            val coreJSON = JSONObject(response.body()!!.string())
                            val core = Create.core(coreJSON)
                            cores.add(core)

                            if (counter == (coreIds.size - 1)) {
                                setViews(cores)
                            }
                            counter += 1

                        } catch (e: Exception) {
                        }
                    }
                })
            }
        } else {
            mView.no_data_cores.visibility = View.VISIBLE
            mView.rv_cores_details.visibility = View.GONE
            mView.progress_cores_details.visibility = View.GONE
        }
    }

    fun setViews(cores: ArrayList<Core>) {
        Handler(Looper.getMainLooper()).post {
            when (cores.size) {
                1 -> {
                    val inflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    mView = inflater.inflate(R.layout.core_details_template, null)
                    val rootView = view as ViewGroup
                    rootView.removeAllViews()
                    rootView.addView(mView)
                    ViewUtils.setViewCoreDetails(mView, cores[0], context!!)
                }
                else -> {
                    mRecyclerView = mView.findViewById(R.id.rv_cores_details)
                    mRecyclerView?.layoutManager = LinearLayoutManager(mView.context)
                    val adapter = AdapterMainViews(mView.context, cores, SpaceXObject.CORE, R.layout.core_list_item)
                    mRecyclerView?.adapter = adapter
                    adapter.notifyDataSetChanged()
                    mView.progress_cores_details.visibility = View.GONE
                }
            }
        }
    }
}