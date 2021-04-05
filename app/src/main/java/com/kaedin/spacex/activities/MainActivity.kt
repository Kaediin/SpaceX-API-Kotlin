package com.kaedin.spacex.activities

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.navigation.NavigationView
import com.kaedin.spacex.R
import com.kaedin.spacex.adapters.PagerAdapterHome
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private var mViewpager: ViewPager? = null
    private var mToolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val pagerAdapter = PagerAdapterHome(supportFragmentManager)
        viewpager_main_home.adapter = pagerAdapter
        viewpager_main_home.offscreenPageLimit = 10
        mViewpager = viewpager_main_home

        tabs_main_home.setupWithViewPager(viewpager_main_home)
        tabs_main_home.tabTextColors = resources.getColorStateList(android.R.color.white)

        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        toolbar.title = "Launches"
        mToolbar = toolbar
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        toggle = ActionBarDrawerToggle(
            this,
            drawer,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        mViewpager?.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                toolbar.title = getTitleToolbar(position)
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    fun getTitleToolbar(position : Int) : String {
        when(position){
            0 -> return "Launches"
            1 -> return "Rockets"
            2 -> return "Dragons"
            3 -> return "Landingpads"
            4 -> return "Launchpads"
            5 -> return "Payloads"
            6 -> return "Ships"
            7 -> return "Capsules"
            8 -> return "Cores"
            9 -> return "Crew"
        }
        return ""
    }


    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_item_zero -> {
                mViewpager?.setCurrentItem(0, true)
                mToolbar?.title = getTitleToolbar(0)
            }
            R.id.nav_item_one -> {
                mViewpager?.setCurrentItem(1, true)
                mToolbar?.title = getTitleToolbar(1)
            }
            R.id.nav_item_two -> {
                mViewpager?.setCurrentItem(2, true)
                mToolbar?.title = getTitleToolbar(2)
            }
            R.id.nav_item_three -> {
                mViewpager?.setCurrentItem(3, true)
                mToolbar?.title = getTitleToolbar(3)
            }
            R.id.nav_item_four -> {
                mViewpager?.setCurrentItem(4, true)
                mToolbar?.title = getTitleToolbar(4)
            }
            R.id.nav_item_five -> {
                mViewpager?.setCurrentItem(5, true)
                mToolbar?.title = getTitleToolbar(5)
            }
            R.id.nav_item_six -> {
                mViewpager?.setCurrentItem(6, true)
                mToolbar?.title = getTitleToolbar(6)
            }
            R.id.nav_item_seven -> {
                mViewpager?.setCurrentItem(7, true)
                mToolbar?.title = getTitleToolbar(7)
            }
            R.id.nav_item_eight -> {
                mViewpager?.setCurrentItem(8, true)
                mToolbar?.title = getTitleToolbar(8)
            }
            R.id.nav_item_nine -> {
                mViewpager?.setCurrentItem(9, true)
                mToolbar?.title = getTitleToolbar(9)
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}