package com.kaedin.spacex.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.spacex.R
import com.kaedin.spacex.models.Payload
import com.kaedin.spacex.utils.ViewUtils
import java.util.*

class AdapterPayloads(private val context: Context,
                      private val payloads : ArrayList<Payload>
) :
    RecyclerView.Adapter<AdapterPayloads.ViewHolder>(){

    class ViewHolder(var cardView: CardView) : RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val rel = LayoutInflater.from(parent.context).inflate(
            R.layout.payload_list_item,
            parent,
            false
        ) as CardView
        return ViewHolder(rel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val payload = payloads[position]
        ViewUtils.setViewPayloads(holder.cardView, context, payload)
    }
    override fun getItemCount() = payloads.size
}