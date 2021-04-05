package com.kaedin.spacex.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.spacex.R
import com.kaedin.spacex.holders.ViewHolder
import com.kaedin.spacex.models.Crew
import com.kaedin.spacex.utils.ViewUtils
import com.kaedin.spacex.utils.WideViewTemplateType
import java.util.*
import kotlin.collections.ArrayList

class AdapterCrew(private val context: Context,
                  private val crews : ArrayList<Crew>) :
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
        val crew = crews[position]
        val images = ArrayList<String>()
        images.add(crew.image!!)
        ViewUtils.setWideViewTemplate(holder, context, images, crew.id!!, crew.name!!, WideViewTemplateType.CREW)
    }

    override fun getItemCount() = crews.size


}