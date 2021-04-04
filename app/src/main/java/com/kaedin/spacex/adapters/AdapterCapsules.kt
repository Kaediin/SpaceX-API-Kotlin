package com.kaedin.spacex.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.spacex.R
import com.kaedin.spacex.models.Capsule
import com.kaedin.spacex.utils.ViewUtils
import java.util.*

class AdapterCapsules(
    private val context: Context,
    private val capsules: ArrayList<Capsule>
) :
    RecyclerView.Adapter<AdapterCapsules.ViewHolder>() {

    class ViewHolder(var cardView: CardView) : RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val rel = LayoutInflater.from(parent.context).inflate(
            R.layout.capsule_list_item,
            parent,
            false
        ) as CardView
        return ViewHolder(rel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val capsule = capsules[position]
        ViewUtils.setViewCapsules(holder.cardView, context,  capsule)
    }

    override fun getItemCount() = capsules.size
}