package com.kaedin.api

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kaedin.api.adapters.PagerAdapterDetails
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar!!.hide()
        val pagerAdapter = PagerAdapterDetails(supportFragmentManager)
        viewpager_main_details.adapter = pagerAdapter

        tabs_main_details.setupWithViewPager(viewpager_main_details)
        tabs_main_details.tabTextColors = resources.getColorStateList(android.R.color.white)
    }

    fun onDataSetChanged() {
        if (viewpager_main_details != null) {
            Thread {
                this.runOnUiThread {
                    viewpager_main_details.adapter!!.notifyDataSetChanged()
                }
            }
        }
    }

}