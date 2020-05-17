package com.example.api.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.api.Create
import com.example.api.DetailActivity
import com.example.api.R
import com.example.api.model.LaunchDetails
import com.example.api.model.Links
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.launch_info_template.*
import kotlinx.android.synthetic.main.launch_info_template.view.*
import okhttp3.*
import org.json.JSONArray
import java.io.IOException


class launchInfoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.launch_info_template, container, false)
        display(view)
        return view
    }

    fun display(view: View) {
        val id = activity!!.intent.extras!!.getInt("launch", -1)

        val client = OkHttpClient()
        val url = "https://api.spacexdata.com/v3/launches"
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {

                val json_array = JSONArray(response.body()!!.string())
                for (i in 0 until json_array.length()) {
                    // JSON vars
                    val launchJSON = json_array.getJSONObject(i)
                    val fl = launchJSON.getInt("flight_number")
                    if (id == fl) {
                        val launch = Create.launchDetail(launchJSON)

                        setView(view, launch)
                        break
                    }
                }
            }
        })

    }

    fun setView(view: View, launch: LaunchDetails) {
        (activity as DetailActivity?)?.runOnUiThread {
            view.progress_details.visibility = View.GONE
            view.rel_layout_detail.visibility = View.VISIBLE
            view.lin_layout_buttons.visibility = View.VISIBLE

            setView(view, launch.links)

            view.missionName.text = launch.mission
            view.launchSite.text = launch.launch_site
            view.mission_patch.setImageBitmap(launch.mission_patch_small)

            view.success.text = if (launch.launch_success.toBoolean()) "Launch succeeded" else "Launch failed"
            view.success.setTextColor(if (launch.launch_success.toBoolean()) resources.getColor(R.color.d_green) else resources.getColor(R.color.d_red))
            view.details.text = launch.details
            (activity as DetailActivity?)?.onDataSetChanged()
        }
    }

    fun setView(view : View, links: Links?){
        (activity as DetailActivity?)?.runOnUiThread {

            view.youtube_details.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + links!!.youtube))
                if (intent.resolveActivity(context!!.packageManager )!= null) {
                    context?.startActivity(intent)
                } else {
                    Snackbar.make(rel_layout_detail, "Unable to locate content", Snackbar.LENGTH_SHORT).show()
                }
            }

            view.reddit_details.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(links!!.reddit))
                if (intent.resolveActivity(context!!.packageManager )!= null) {
                    context?.startActivity(intent)
                } else {
                    Snackbar.make(rel_layout_detail, "Unable to locate content", Snackbar.LENGTH_SHORT).show()
                }
            }

            view.news_details.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(links!!.news))
                if (intent.resolveActivity(context!!.packageManager )!= null) {
                    context?.startActivity(intent)
                } else {
                    Snackbar.make(rel_layout_detail, "Unable to locate content", Snackbar.LENGTH_SHORT).show()
                }
            }

            view.wiki_details.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(links!!.wikipedia))
                if (intent.resolveActivity(context!!.packageManager )!= null) {
                    context?.startActivity(intent)
                } else {
                    Snackbar.make(rel_layout_detail, "Unable to locate content", Snackbar.LENGTH_SHORT).show()
                }
            }

            view.presskit_details.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(links!!.spacex))
                if (intent.resolveActivity(context!!.packageManager )!= null){
                    context?.startActivity(intent)
                } else {
                    Snackbar.make(rel_layout_detail, "Unable to locate content", Snackbar.LENGTH_SHORT).show()
                }
            }
            (activity as DetailActivity?)?.onDataSetChanged()
        }

    }
}