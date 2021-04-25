package com.kaedin.spacex.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.spacex.holders.ViewHolder
import com.kaedin.spacex.models.*
import com.kaedin.spacex.utils.SpaceXObject
import com.kaedin.spacex.utils.ViewUtils
import com.kaedin.spacex.utils.WideViewTemplateType
import java.util.*
import kotlin.collections.ArrayList

class AdapterMainViews(
    private val context: Context,
    private var objects: ArrayList<*>,
    private val spaceXObject: SpaceXObject,
    private val layoutResource : Int
) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val rel = LayoutInflater.from(parent.context).inflate(
            layoutResource,
            parent,
            false
        ) as CardView
        return ViewHolder(rel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (spaceXObject){
            SpaceXObject.CAPSULE -> ViewUtils.setViewCapsules(holder.cardView, context,  objects[position] as? Capsule)
            SpaceXObject.CORE -> ViewUtils.setViewCores(holder.cardView, context,  objects[position] as? Core)
            SpaceXObject.SHIP -> ViewUtils.setViewShips(holder.cardView, context, objects[position] as? Ship)
            SpaceXObject.LAUNCH -> ViewUtils.setViewLaunches(holder.cardView, context, objects[position] as? Launch)
            SpaceXObject.PAYLOAD -> ViewUtils.setViewPayloads(holder.cardView, context, objects[position] as? Payload)
            SpaceXObject.CREW -> {
                val crew = objects[position] as Crew
                val images = ArrayList<String>(listOf(crew.image))
                ViewUtils.setWideViewTemplate(holder, context, images, crew.id!!, crew.name!!, WideViewTemplateType.CREW)
            }
            SpaceXObject.DRAGON -> {
                val dragon = objects[position] as Dragon
                ViewUtils.setWideViewTemplate(holder, context, dragon.flickrImages, dragon.id, dragon.name!!, WideViewTemplateType.DRAGON)
            }
            SpaceXObject.ROCKET -> {
                val rocket = objects[position] as Rocket
                ViewUtils.setWideViewTemplate(holder, context, rocket.flickr_images, rocket.id, rocket.name!!, WideViewTemplateType.ROCKET)
            }
        }
    }

    fun updateAdapterLaunches(launches : ArrayList<Launch>){
        this.objects = launches
        notifyDataSetChanged()
    }

    override fun getItemCount() = objects.size
}