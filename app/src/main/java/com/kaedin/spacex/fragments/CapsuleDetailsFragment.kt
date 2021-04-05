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
//import com.kaedin.spacex.adapters.AdapterCapsules
import com.kaedin.spacex.models.Capsule
import com.kaedin.spacex.utils.Create
import com.kaedin.spacex.utils.SpaceXObject
import com.kaedin.spacex.utils.ViewUtils
import kotlinx.android.synthetic.main.capsule_details_template.view.*
import kotlinx.android.synthetic.main.capsules_rv_details_template.view.*
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class CapsuleDetailsFragment : Fragment() {

    private lateinit var mView: View
    private var mRecyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.capsules_rv_details_template, container, false)
        mView = view
        display()
        return view
    }

    fun display() {
        val capsuleIds = activity!!.intent.extras!!.getStringArrayList("capsule_ids")
        val capsules = ArrayList<Capsule>()
        println("Capsule ids: $capsuleIds")
        var counter = 0
        if (capsuleIds!!.isNotEmpty()) {
            for (id in capsuleIds.iterator()) {
                val client = OkHttpClient()
                val url = "https://api.spacexdata.com/v4/capsules/$id"
                val request = Request.Builder()
                    .url(url)
                    .build()

                client.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {}
                    override fun onResponse(call: Call, response: Response) {
                        try {
                            val capsuleJSON = JSONObject(response.body()!!.string())
                            val capsule = Create.capsule(capsuleJSON)
                            capsules.add(capsule)

                            if (counter == (capsuleIds.size - 1)) {
                                setViews(capsules)
                            }
                            counter += 1

                        } catch (e: Exception) {
                        }
                    }
                })
            }
        } else {
            mView.no_data_capsules.visibility = View.VISIBLE
            mView.rv_capsules_details.visibility = View.GONE
            mView.progress_capsules_details.visibility = View.GONE
        }
    }

    fun setViews(capsules: ArrayList<Capsule>) {
        Handler(Looper.getMainLooper()).post {
            when (capsules.size) {
                1 -> {
                    val inflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                    mView = inflater.inflate(R.layout.capsule_details_template, null)
                    val rootView = view as ViewGroup
                    rootView.removeAllViews()
                    rootView.addView(mView)
                    ViewUtils.setViewCapsuleDetails(mView, capsules[0], context!!)
                }
                else -> {
                    mRecyclerView = mView.findViewById(R.id.rv_capsules_details)
                    mRecyclerView?.layoutManager = LinearLayoutManager(mView.context)
                    val adapter = AdapterMainViews(mView.context, capsules, SpaceXObject.CAPSULE, R.layout.capsule_list_item)
                    mRecyclerView?.adapter = adapter
                    adapter.notifyDataSetChanged()
                    mView.progress_capsules_details.visibility = View.GONE
                }
            }
        }
    }
}