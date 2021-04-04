package com.kaedin.spacex.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.spacex.R
import com.kaedin.spacex.models.Core
import com.kaedin.spacex.utils.ViewUtils
import java.util.*

class AdapterCores(
    private val context: Context,
    private val cores: ArrayList<Core>
) :
    RecyclerView.Adapter<AdapterCores.ViewHolder>() {

    class ViewHolder(var cardView: CardView) : RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val rel = LayoutInflater.from(parent.context).inflate(
            R.layout.core_list_item,
            parent,
            false
        ) as CardView
        return ViewHolder(rel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val core = cores[position]
        ViewUtils.setViewCores(holder.cardView, context,  core)
    }

    override fun getItemCount() = cores.size
}