package com.kaedin.api

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar!!.hide()
        val pagerAdapter = PagerAdapter(supportFragmentManager)
        viewpager_main.adapter = pagerAdapter

        tabs_main.setupWithViewPager(viewpager_main)
        tabs_main.tabTextColors = resources.getColorStateList(android.R.color.white)

    }

    fun onDataSetChanged() {
        if (viewpager_main != null) {
            Thread(Runnable {
                this.runOnUiThread {
                    viewpager_main.adapter!!.notifyDataSetChanged()
                }
            })
        }
    }

}