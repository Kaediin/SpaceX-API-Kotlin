package com.kaedin.api.adapters

import Payload
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.api.R
import kotlinx.android.synthetic.main.second_stage_list_item.view.*

class AdapterPayload(private val context: Context,
                     private val payloads: ArrayList<Payload>?) :
    RecyclerView.Adapter<AdapterPayload.ViewHolder>() {

    class ViewHolder(val relativeLayout: RelativeLayout) : RecyclerView.ViewHolder(relativeLayout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val rel = LayoutInflater.from(parent.context).inflate(
            R.layout.second_stage_list_item,
            parent,
            false
        ) as RelativeLayout
        return ViewHolder(rel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

//        setBoolViews(holder, position)
        holder.relativeLayout.payload_header.text = "Payload ${position+1}"
        holder.relativeLayout.payload_id.text = payloads?.get(position)?.payload_id
        holder.relativeLayout.reused_second.text = payloads?.get(position)?.reused
        holder.relativeLayout.customers.text = payloads?.get(position)?.customers
        holder.relativeLayout.nationality.text = payloads?.get(position)?.nationality
        holder.relativeLayout.manufacturer.text = payloads?.get(position)?.manufacturer
        holder.relativeLayout.payload_type.text = payloads?.get(position)?.payload_type
        holder.relativeLayout.payload_mass.text = payloads?.get(position)?.payload_mass_kg
        holder.relativeLayout.orbit.text = payloads?.get(position)?.orbit

    }

    override fun getItemCount(): Int {
        return if (payloads?.size!! > 0){
            payloads.size
        } else {
            0
        }
    }
}