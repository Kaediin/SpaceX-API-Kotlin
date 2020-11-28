package com.kaedin.api.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kaedin.api.models.Launch
import com.google.android.material.snackbar.Snackbar
import com.kaedin.api.DetailActivity
import com.kaedin.api.R
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.ArrayList

class AdapterLaunch(private val context: Context,
                    private val launches : ArrayList<Launch>) :
    RecyclerView.Adapter<AdapterLaunch.ViewHolder>(){

    class ViewHolder(val relativeLayout: RelativeLayout) : RecyclerView.ViewHolder(relativeLayout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val rel = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item,
            parent,
            false
        ) as RelativeLayout
        return ViewHolder(rel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.relativeLayout.pattern.visibility = View.VISIBLE
        holder.relativeLayout.flight_details.setTextColor(Color.BLACK)

        val id = launches[position].id
        val details = launches[position].details
        val mission = launches[position].mission
        val missionsPatchUrl : String? = launches[position].mission_patch_url
        val rocket = launches[position].rocket
        val upcoming = launches[position].upcoming
        val date = launches[position].launch_date

        holder.relativeLayout.flight_details.text = Html.fromHtml(details.substring(0, details.length.coerceAtMost(125)) +"... <br><b>[Read more!]</b>")

        if(upcoming){
            if (details == "null"){
                holder.relativeLayout.flight_details.setTextColor(Color.RED)
                holder.relativeLayout.flight_details.text = context.resources.getString(R.string.upcoming)
            }
        } else {
            if (details == "null"){
                holder.relativeLayout.flight_details.text = context.resources.getString(R.string.no_deatils_available)
            }
        }

        if (missionsPatchUrl != null && missionsPatchUrl != "null"){
            Glide.with(context)
                .load(missionsPatchUrl)
                .into(holder.relativeLayout.pattern)
        } else {
            holder.relativeLayout.pattern.visibility = View.GONE
        }

        holder.relativeLayout.mission_name.text = mission
        holder.relativeLayout.rocket_name.text = rocket
        holder.relativeLayout.date_launch.text = date

        holder.relativeLayout.list_item.setOnClickListener {
            if (!upcoming) {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("launch", id)
                context.startActivity(intent)
            } else {
                Snackbar.make(
                    holder.relativeLayout.list_item,
                    "This launch is upcoming. There are no details yet",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun getItemCount() = launches.size


}