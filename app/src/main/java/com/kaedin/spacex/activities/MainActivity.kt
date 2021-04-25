package com.kaedin.spacex.activities

import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.android.billingclient.api.*
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.kaedin.spacex.R
import com.kaedin.spacex.adapters.PagerAdapterHome
import com.kaedin.spacex.controllers.BillingController
import com.kaedin.spacex.fragments.LaunchesFragment
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.donation_views.view.*
import kotlinx.android.synthetic.main.donations.view.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var drawer: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle
    private var mViewpager: ViewPager? = null
    private var mToolbar: Toolbar? = null
    private var mAppUpdateManager: AppUpdateManager? = null
    private val RC_APP_UPDATE = 11
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPrefs.registerOnSharedPreferenceChangeListener(this)
        setupHomeScreen()
    }

    fun setupHomeScreen() {
        setupToolbar()
        setupViewPager(sharedPrefs.getBoolean(getString(R.string.prefs_show_nav_bar), false))
    }

    fun setupToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar_main)
        toolbar.title = "Launches"
        mToolbar = toolbar
        setSupportActionBar(toolbar)
        drawer = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)
        val header: View = navigationView.getHeaderView(0)
        header.nav_header_donate.setOnClickListener {
            BillingController(this@MainActivity, this).run()
        }

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

    fun changeVisibilityTabs(showTabs: Boolean) {
        tabs_main_home.visibility = if (showTabs) View.VISIBLE else View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(this)
    }

    fun setupViewPager(showTabs: Boolean) {
        val pagerAdapter = PagerAdapterHome(supportFragmentManager)
        viewpager_main_home.adapter = pagerAdapter
        viewpager_main_home.offscreenPageLimit = 10
        mViewpager = viewpager_main_home
        tabs_main_home.visibility = if (showTabs) View.VISIBLE else View.GONE

        tabs_main_home.setupWithViewPager(viewpager_main_home)
        tabs_main_home.tabTextColors =
            AppCompatResources.getColorStateList(this, android.R.color.white)
    }

    fun getTitleToolbar(position: Int): String {
        when (position) {
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

    override fun onStart() {
        super.onStart()
        println("(MainActivity - onStart)")
        mAppUpdateManager = AppUpdateManagerFactory.create(this)

        mAppUpdateManager?.registerListener(installStateUpdatedListener)
        println("(MainActivity - onStart) mAppUpdateManager registered")


        mAppUpdateManager?.appUpdateInfo?.addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE /*AppUpdateType.IMMEDIATE*/)
            ) {
                try {
                    println("(MainActivity - onStart) update found")
                    mAppUpdateManager?.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE /*AppUpdateType.IMMEDIATE*/,
                        this@MainActivity,
                        RC_APP_UPDATE
                    )
                } catch (e: SendIntentException) {
                    e.printStackTrace()
                }
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                popupSnackbarForCompleteUpdate()
            }
        }?.addOnFailureListener { failure ->
            println("(MainActivity - onStart) failure: $failure")
        }
    }

    private var installStateUpdatedListener: InstallStateUpdatedListener =
        object : InstallStateUpdatedListener {
            override fun onStateUpdate(state: InstallState) {
                if (state.installStatus() == InstallStatus.DOWNLOADED) {
                    //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                    popupSnackbarForCompleteUpdate()
                } else if (state.installStatus() == InstallStatus.INSTALLED) {
                    if (mAppUpdateManager != null) {
                        mAppUpdateManager!!.unregisterListener(this)
                    }
                } else {
                    Log.i(
                        "MainActivity",
                        "InstallStateUpdatedListener: state: " + state.installStatus()
                    )
                }
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.e("MainActivity", "onActivityResult: app download failed")
            }
        }
    }

    private fun popupSnackbarForCompleteUpdate() {
        val snackbar = Snackbar.make(
            findViewById(R.id.drawer_layout),
            "New app is ready!",
            Snackbar.LENGTH_INDEFINITE
        )
        snackbar.setAction("Install") { view: View? ->
            if (mAppUpdateManager != null) {
                mAppUpdateManager!!.completeUpdate()
            }
        }
        snackbar.show()
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

    override fun onStop() {
        super.onStop()
        if (mAppUpdateManager != null) {
            mAppUpdateManager?.unregisterListener(installStateUpdatedListener);
        }
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
            R.id.nav_item_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
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

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (sharedPreferences != null) {
            when (key) {
                getString(R.string.prefs_show_nav_bar) -> {
                    changeVisibilityTabs(sharedPreferences.getBoolean(key, false))
                }
            }
        }
    }

}