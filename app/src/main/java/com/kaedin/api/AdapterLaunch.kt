package com.kaedin.api

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.api.model.Launch
import com.google.android.material.snackbar.Snackbar
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

        val id = launches.get(position).id
        val details = launches.get(position).details
        val mission = launches.get(position).mission
        val missions_small_patch : Bitmap? = launches.get(position).mission_patch_small
        val rocket = launches.get(position).rocket
        val upcoming = launches.get(position).upcoming
        val date = launches.get(position).launch_date

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

        if (missions_small_patch != null){
            holder.relativeLayout.pattern.setImageBitmap(missions_small_patch)
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