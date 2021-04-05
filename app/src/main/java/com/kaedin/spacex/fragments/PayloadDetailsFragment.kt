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
//import com.kaedin.spacex.adapters.AdapterPayloads
//import com.kaedin.spacex.adapters.AdapterShips
import com.kaedin.spacex.models.Payload
import com.kaedin.spacex.models.Ship
import com.kaedin.spacex.utils.Create
import com.kaedin.spacex.utils.SpaceXObject
import com.kaedin.spacex.utils.ViewUtils
import kotlinx.android.synthetic.main.payloads_rv_details_template.view.*
import kotlinx.android.synthetic.main.ships_rv_details_template.view.*
import kotlinx.android.synthetic.main.ships_rv_details_template.view.progress_ships_details
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class PayloadDetailsFragment : Fragment() {

    private var mView: View? = null
    private var mRecyclerView: RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.payloads_rv_details_template, container, false)
        mView = view
        display()
        return view
    }

    fun display() {
        val payloadIds = activity!!.intent.extras!!.getStringArrayList("payload_ids")
        val iterator = payloadIds!!.iterator()
        val payloads = ArrayList<Payload>()
        println("Payload ids: $payloadIds")
        while (iterator.hasNext()) {
            val payloadId = iterator.next()
            val client = OkHttpClient()
            val url = "https://api.spacexdata.com/v4/payloads/$payloadId"
            val request = Request.Builder()
                .url(url)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val payloadJSON = JSONObject(response.body()!!.string())
                        val payload = Create.payload(payloadJSON)
                        payloads.add(payload)

                        if (!iterator.hasNext()) {
                            setViews(payloads)
                        }

                    } catch (e: Exception) {
                    }
                }
            })
        }
    }

    fun setViews(payloads: ArrayList<Payload>) {
        Handler(Looper.getMainLooper()).post {
            if (payloads.size == 1){
                val inflater = activity!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                mView = inflater.inflate(R.layout.payload_details_template, null)
                val rootView = view as ViewGroup
                rootView.removeAllViews()
                rootView.addView(mView)
                ViewUtils.setViewPayloadDetails(mView!!, payloads[0], context!!)
            } else {
                mRecyclerView = mView?.findViewById(R.id.rv_payloads_details)
                mRecyclerView?.layoutManager = LinearLayoutManager(context)
                val adapter = AdapterMainViews(mView!!.context, payloads, SpaceXObject.PAYLOAD, R.layout.payload_list_item)
                mRecyclerView?.adapter = adapter
                adapter.notifyDataSetChanged()
                mView?.progress_payloads_details?.visibility = View.GONE
            }
        }
    }
}