package com.kaedin.api.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.api.R
import com.kaedin.api.models.Core
import kotlinx.android.synthetic.main.first_stage_list_item.view.*

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
        holder.relativeLayout.core_header.text = "Core ${position+1}"
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

    override fun getItemCount(): Int {
        return if (cores?.size!! > 0){
            cores.size
        } else {
            0
        }
    }
}