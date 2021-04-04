package com.kaedin.spacex.adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.kaedin.spacex.R
import com.kaedin.spacex.activities.LaunchDetailActivity
import com.kaedin.spacex.models.Launch
import com.kaedin.spacex.utils.DataUtils
import kotlinx.android.synthetic.main.launch_list_item.view.*
import java.util.*
import kotlin.collections.ArrayList

class AdapterLaunches(
    private val context: Context,
    private val launches: ArrayList<Launch>
) :
    RecyclerView.Adapter<AdapterLaunches.ViewHolder>() {

    class ViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val rel = LayoutInflater.from(parent.context).inflate(
            R.layout.launch_list_item,
            parent,
            false
        ) as CardView
        return ViewHolder(rel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.cardView.pattern.visibility = View.VISIBLE
        holder.cardView.flight_details.setTextColor(Color.BLACK)

        val id = launches[position].id
        val rocketId = launches[position].rocket_id
        val details = launches[position].details
        val mission = launches[position].name
        val missionsPatchUrl: String? = launches[position].links?.mission_patch_small
        val upcoming = launches[position].upcoming
        val date = launches[position].date_unix

        holder.cardView.flight_details.text = details

        holder.cardView.flight_details.setTextColor(Color.WHITE)

        if (upcoming!!) {
            if (details == "null") {
                holder.cardView.flight_details.setTextColor(Color.RED)
                holder.cardView.flight_details.text = context.resources.getString(R.string.upcoming)
            }
        } else {
            if (details == "null") {
                holder.cardView.flight_details.text =
                    context.resources.getString(R.string.no_deatils_available)
            }
        }

        if (missionsPatchUrl != null && missionsPatchUrl != "null") {
            Glide.with(context)
                .load(missionsPatchUrl)
                .into(holder.cardView.pattern)
        } else {
            holder.cardView.pattern.visibility = View.GONE
        }

        holder.cardView.mission_name.text = mission
        holder.cardView.date_launch.text = DataUtils.getDateTime(date.toString())
        holder.cardView.flight_number.text = "Flight: ${launches[position].flight_number}"

        holder.cardView.list_item.setOnClickListener {
            if (!upcoming) {
                val intent = Intent(context, LaunchDetailActivity::class.java)
                intent.putExtra("launch_id", id)
                intent.putExtra("rocket_id", rocketId)
                intent.putStringArrayListExtra("payload_ids", launches[position].payload_ids)
                intent.putStringArrayListExtra("ship_ids", launches[position].ship_ids)
                intent.putStringArrayListExtra("capsule_ids", launches[position].capsule_ids)
                intent.putStringArrayListExtra("core_ids", launches[position].cores)
                intent.putExtra("launchpad_id", launches[position].launchpad_id)
                context.startActivity(intent)
            } else {
                Snackbar.make(
                    holder.cardView.list_item,
                    "This launch is upcoming. There are no details yet",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        holder.cardView.mission_name.post {
            val lineCnt: Int = holder.cardView.mission_name.lineCount * 2
            val standardLines = if (lineCnt == 4) 9 else 8
            val leftoverSpace = standardLines - lineCnt
            holder.cardView.flight_details.maxLines = leftoverSpace
            // Perform any actions you want based on the line count here.
        }

    }

    override fun getItemCount() = launches.size


}