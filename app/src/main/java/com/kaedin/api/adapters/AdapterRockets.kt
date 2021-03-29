package com.kaedin.api.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kaedin.api.R
import com.kaedin.api.RocketDetailActivity
import com.kaedin.api.models.Rocket
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener
import kotlinx.android.synthetic.main.rocket_list_item.view.*
import java.util.*

class AdapterRockets(private val context: Context,
                    private val rockets : ArrayList<Rocket>
) :
    RecyclerView.Adapter<AdapterRockets.ViewHolder>(){

    class ViewHolder(val relativeLayout: RelativeLayout) : RecyclerView.ViewHolder(relativeLayout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val rel = LayoutInflater.from(parent.context).inflate(
            R.layout.rocket_list_item,
            parent,
            false
        ) as RelativeLayout
        return ViewHolder(rel)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val carousel: CarouselView = holder.relativeLayout.findViewById(R.id.rocket_carousel)

        val imageListener = ImageListener {pos, imageView ->
//            imageView.scaleType = ImageView.ScaleType.FIT_CENTER
            Glide.with(context)
                .load(rockets[position].flickr_images!![pos])
                .into(imageView)
            imageView.setOnClickListener {
                launchRocketDetails(rockets[position].id)
            }
        }
        carousel.setImageListener(imageListener)
        carousel.pageCount = rockets[position].flickr_images!!.size
        holder.relativeLayout.rocket_name.text = rockets[position].name

        holder.relativeLayout.rocket_list_item.setOnClickListener {
            launchRocketDetails(rockets[position].id)
        }
    }

    fun launchRocketDetails(rocketId : String){
        val intent = Intent(context, RocketDetailActivity::class.java)
        intent.putExtra("rocket_id", rocketId)
        context.startActivity(intent)
    }

    override fun getItemCount() = rockets.size


}