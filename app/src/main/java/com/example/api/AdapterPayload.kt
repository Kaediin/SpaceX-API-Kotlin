package com.example.api

import Payload
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.api.model.SecondStage
import kotlinx.android.synthetic.main.first_stage_list_item.view.*
import kotlinx.android.synthetic.main.second_stage_list_item.view.*

class AdapterPayload(private val context: Context,
                     private val payloads: ArrayList<Payload>?) :
    RecyclerView.Adapter<AdapterPayload.ViewHolder>() {

    class ViewHolder(val relativeLayout: RelativeLayout) : RecyclerView.ViewHolder(relativeLayout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterPayload.ViewHolder {
        val rel = LayoutInflater.from(parent.context).inflate(
            R.layout.second_stage_list_item,
            parent,
            false
        ) as RelativeLayout
        return ViewHolder(rel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        setBoolViews(holder, position)
        holder.relativeLayout.payload_header.text = "Payload "+(position+1)
        holder.relativeLayout.payload_id.text = payloads?.get(position)?.payload_id
        holder.relativeLayout.reused_second.text = payloads?.get(position)?.reused
        holder.relativeLayout.customers.text = payloads?.get(position)?.customers
        holder.relativeLayout.nationality.text = payloads?.get(position)?.nationality
        holder.relativeLayout.manufacturer.text = payloads?.get(position)?.manufacturer
        holder.relativeLayout.payload_type.text = payloads?.get(position)?.payload_type
        holder.relativeLayout.payload_mass.text = payloads?.get(position)?.payload_mass_kg
        holder.relativeLayout.orbit.text = payloads?.get(position)?.orbit

    }

//    fun setBoolViews(holder: ViewHolder, position: Int){
//
//        holder.relativeLayout.reused_second.setTextColor(if (!payloads!![position].reused!!.toBoolean()) context.resources.getColor(R.color.d_red) else context.resources.getColor(R.color.d_green))
//        holder.relativeLayout.reused_second_title.setTextColor(if (!payloads[position].reused!!.toBoolean()) context.resources.getColor(R.color.d_red) else context.resources.getColor(R.color.d_green))
//    }

    override fun getItemCount(): Int {
        if (payloads?.size!! > 0){
            return payloads.size
        } else {
            return 0
        }
    }
}