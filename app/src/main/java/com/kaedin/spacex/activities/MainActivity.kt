package com.kaedin.spacex.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kaedin.spacex.R
import com.kaedin.spacex.adapters.PagerAdapterHome
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.NullPointerException
import java.lang.RuntimeException

class MainActivity : AppCompatActivity(){

    @SuppressLint("UseCompatLoadingForColorStateLists")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()
        val pagerAdapter = PagerAdapterHome(supportFragmentManager)
        viewpager_main_home.adapter = pagerAdapter
        viewpager_main_home.offscreenPageLimit = 10

        tabs_main_home.setupWithViewPager(viewpager_main_home)
        tabs_main_home.tabTextColors = resources.getColorStateList(android.R.color.white)

    }



    fun onDataSetChanged() {
        if (viewpager_main_home != null) {
            Thread {
                this.runOnUiThread {
                    viewpager_main_home.adapter!!.notifyDataSetChanged()
                }
            }
        }
    }
}