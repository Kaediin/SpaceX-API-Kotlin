package com.kaedin.spacex.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.spacex.R
import com.kaedin.spacex.holders.ViewHolder
import com.kaedin.spacex.models.Rocket
import com.kaedin.spacex.utils.ViewUtils
import com.kaedin.spacex.utils.WideViewTemplateType
import java.util.*

class AdapterRockets(private val context: Context,
                    private val rockets : ArrayList<Rocket>) :
    RecyclerView.Adapter<ViewHolder>(){

//    class ViewHolder(val relativeLayout: RelativeLayout) : RecyclerView.ViewHolder(relativeLayout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val rel = LayoutInflater.from(parent.context).inflate(
            R.layout.wide_view_list_item,
            parent,
            false
        ) as CardView
        return ViewHolder(rel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rocket = rockets[position]
        ViewUtils.setWideViewTemplate(holder, context, rocket.flickr_images, rocket.id, rocket.name!!, WideViewTemplateType.ROCKET)
    }

    override fun getItemCount() = rockets.size


}