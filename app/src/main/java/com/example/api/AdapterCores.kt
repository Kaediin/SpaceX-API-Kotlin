package com.example.api

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.api.model.Core
import com.example.api.model.Rocket
import kotlinx.android.synthetic.main.first_stage_list_item.view.*
import kotlinx.android.synthetic.main.rocket_template.view.*
import kotlinx.android.synthetic.main.second_stage_list_item.view.*

class AdapterCores(private val context: Context,
                   private val cores: ArrayList<Core>?) :
    RecyclerView.Adapter<AdapterCores.ViewHolder>() {

    class ViewHolder(val relativeLayout: RelativeLayout) : RecyclerView.ViewHolder(relativeLayout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val rel = LayoutInflater.from(parent.context).inflate(
            R.layout.first_stage_list_item,
            parent,
            false
        ) as RelativeLayout
        return ViewHolder(rel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        setBoolViews(holder, position)
        holder.relativeLayout.core_header.text = "Core "+(position+1)
        holder.relativeLayout.core_serial.text = cores?.get(position)?.core_serial
        holder.relativeLayout.flight.text = cores?.get(position)?.flight
        holder.relativeLayout.block.text = cores?.get(position)?.block
        holder.relativeLayout.gridfins.text = cores?.get(position)?.gridfins
        holder.relativeLayout.legs.text = cores?.get(position)?.legs
        holder.relativeLayout.reused_core.text = cores?.get(position)?.reused
        holder.relativeLayout.land_success_core.text = cores?.get(position)?.land_success
        holder.relativeLayout.landing_intent.text = cores?.get(position)?.landing_intent
        holder.relativeLayout.landing_type_intent.text = cores?.get(position)?.landing_type
        holder.relativeLayout.landing_vehicle.text = cores?.get(position)?.landing_vehicle
    }

//    fun setBoolViews(holder: ViewHolder, position: Int){
//
//        holder.relativeLayout.gridfins.setTextColor(if (!cores!![position].reused!!.toBoolean()) context.resources.getColor(R.color.d_red) else context.resources.getColor(R.color.d_green))
//        holder.relativeLayout.gridfins_title.setTextColor(if (!cores[position].reused!!.toBoolean()) context.resources.getColor(R.color.d_red) else context.resources.getColor(R.color.d_green))
//
//        holder.relativeLayout.legs.setTextColor(if (!cores[position].reused!!.toBoolean()) context.resources.getColor(R.color.d_red) else context.resources.getColor(R.color.d_green))
//        holder.relativeLayout.legs_title.setTextColor(if (!cores[position].reused!!.toBoolean()) context.resources.getColor(R.color.d_red) else context.resources.getColor(R.color.d_green))
//
//        holder.relativeLayout.reused_core.setTextColor(if (!cores[position].reused!!.toBoolean()) context.resources.getColor(R.color.d_red) else context.resources.getColor(R.color.d_green))
//        holder.relativeLayout.reused_core_title.setTextColor(if (!cores[position].reused!!.toBoolean()) context.resources.getColor(R.color.d_red) else context.resources.getColor(R.color.d_green))
//
//        holder.relativeLayout.land_success_core.setTextColor(if (!cores[position].reused!!.toBoolean()) context.resources.getColor(R.color.d_red) else context.resources.getColor(R.color.d_green))
//        holder.relativeLayout.land_success_core_title.setTextColor(if (!cores[position].reused!!.toBoolean()) context.resources.getColor(R.color.d_red) else context.resources.getColor(R.color.d_green))
//
//        holder.relativeLayout.landing_intent.setTextColor(if (!cores[position].reused!!.toBoolean()) context.resources.getColor(R.color.d_red) else context.resources.getColor(R.color.d_green))
//        holder.relativeLayout.landing_intent_title.setTextColor(if (!cores[position].reused!!.toBoolean()) context.resources.getColor(R.color.d_red) else context.resources.getColor(R.color.d_green))
//    }

    override fun getItemCount(): Int {
        if (cores?.size!! > 0){
            return cores.size
        } else {
            return 0
        }
    }
}