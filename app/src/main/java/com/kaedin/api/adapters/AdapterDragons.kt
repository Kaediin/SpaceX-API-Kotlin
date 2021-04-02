package com.kaedin.api.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.api.R
import com.kaedin.api.holders.ViewHolder
import com.kaedin.api.models.Dragon
import com.kaedin.api.utils.ViewUtils
import com.kaedin.api.utils.WideViewTemplateType
import java.util.*

class AdapterDragons(private val context: Context,
                     private val dragons : ArrayList<Dragon>
) :
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
        val dragon = dragons[position]
        ViewUtils.setWideViewTemplate(holder, context, dragon.flickrImages, dragon.id, dragon.name!!, WideViewTemplateType.DRAGON)
    }
    override fun getItemCount() = dragons.size
}