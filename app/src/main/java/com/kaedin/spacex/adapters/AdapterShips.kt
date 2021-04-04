package com.kaedin.spacex.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.spacex.R
import com.kaedin.spacex.models.Ship
import com.kaedin.spacex.utils.ViewUtils
import java.util.*

class AdapterShips(
    private val context: Context,
    private val ships: ArrayList<Ship>
) :
    RecyclerView.Adapter<AdapterShips.ViewHolder>() {

    class ViewHolder(var cardView: CardView) : RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val rel = LayoutInflater.from(parent.context).inflate(
            R.layout.ship_list_item,
            parent,
            false
        ) as CardView
        return ViewHolder(rel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ship = ships[position]
        ViewUtils.setViewShips(holder.cardView, context, ship)
    }

    override fun getItemCount() = ships.size
}