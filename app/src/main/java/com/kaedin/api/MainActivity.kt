package com.kaedin.api

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.api.model.Filter
import com.kaedin.api.model.Launch
import com.google.android.material.snackbar.Snackbar
import com.kaedin.api.asynctasks.ApiRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_quality.view.*
import okhttp3.*
import java.io.IOException
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    private lateinit var sp_upcoming: SharedPreferences
    private lateinit var sp_reverse: SharedPreferences
    private lateinit var sp_lowQuality: SharedPreferences

    private lateinit var editor: SharedPreferences.Editor

    private lateinit var adapter: RecyclerView.Adapter<*>

    private var filter: Filter = Filter()

    var currentLaunches = ArrayList<Launch>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        sp_upcoming = getSharedPreferences("show_upcoming", Context.MODE_PRIVATE)
        sp_reverse = getSharedPreferences("reverse_list", Context.MODE_PRIVATE)
        sp_lowQuality = getSharedPreferences("low_quality", Context.MODE_PRIVATE)

        val showUpcoming = sp_upcoming.getBoolean("show_upcoming", true)
        val reverseList = sp_reverse.getBoolean("reverse_list", false)
        val lowQuality = sp_lowQuality.getBoolean("low_quality", false)

        filter.showUpcoming = showUpcoming
        filter.reverseList = reverseList

        run()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val infalter: MenuInflater = menuInflater
        infalter.inflate(R.menu.menu, menu)
        val switch_upcoming: MenuItem = menu!!.findItem(R.id.switch_upcoming)
        if (filter.showUpcoming) {
            switch_upcoming.title = "Hide upcoming"
        } else {
            switch_upcoming.title = "Show upcoming"
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.switch_upcoming -> {
                if (progress_horizontal.visibility == View.VISIBLE) {
                    makeSnackbar("Please wait for the content to load", Snackbar.LENGTH_SHORT)
                    return false
                }
                editor = sp_upcoming.edit()

                if (filter.showUpcoming) {
                    item.title = "Show upcoming"
                    makeSnackbar("Hiding upcoming launches", Snackbar.LENGTH_SHORT)
                } else {
                    item.title = "Hide upcoming"
                    makeSnackbar("Showing upcoming launches", Snackbar.LENGTH_SHORT)
                }

                filter.showUpcoming = !filter.showUpcoming
                editor.putBoolean("show_upcoming", filter.showUpcoming)
                editor.apply()

                currentLaunches = DataUtils.showHideUpcoming(filter)
                display()

                return true
            }
            R.id.reverse_list -> {
                if (progress_horizontal.visibility == View.VISIBLE) {
                    makeSnackbar("Please wait for the content to load", Snackbar.LENGTH_SHORT)
                    return false
                }
                filter.reverseList = !filter.reverseList
                editor = sp_reverse.edit()
                editor.putBoolean("reverse_list", filter.reverseList)
                editor.apply()

                currentLaunches = DataUtils.reverseList(currentLaunches)


                display()
                makeSnackbar("List reversed", Snackbar.LENGTH_SHORT)
                return true
            }
            R.id.refresh -> {
                if (progress_horizontal.visibility == View.VISIBLE) {
                    makeSnackbar("Please wait for the content to load", Snackbar.LENGTH_SHORT)
                    return false
                }
                makeSnackbar("Refreshed", Snackbar.LENGTH_SHORT)
                display()
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    fun run() {
        val url = "https://api.spacexdata.com/v3/launches"
        val request = Request.Builder()
            .url(url)
            .build()

        updateProgressBar(0, "Trying to establishing a connection with SpaceX", null)

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                makeSnackbar(e.toString(), Snackbar.LENGTH_INDEFINITE)
                progress_horizontal.visibility = View.GONE
            }

            override fun onResponse(call: Call, response: Response) {
                ApiRequest(this@MainActivity, filter).execute(response)
            }
        })

    }


    fun display() {
        Thread {
            this.runOnUiThread {
                val recyclerView = findViewById<RecyclerView>(R.id.rv)
                recyclerView.layoutManager = LinearLayoutManager(this)
                adapter = AdapterLaunch(this, currentLaunches)
                recyclerView.adapter = adapter
                text_progress.visibility = View.GONE
                progress_horizontal.visibility = View.GONE
                adapter.notifyDataSetChanged()
            }
        }.start()
    }

    fun makeSnackbar(message: String, snackbarLength: Int) {
        Snackbar.make(
            this.findViewById(R.id.mainActivity),
            message,
            snackbarLength
        ).show()
    }

    fun updateProgressBar(progress: Int, text: String, limit: Int?) {
        Thread {
            this.runOnUiThread {
                text_progress.text = text
                progress_horizontal.progress = progress
                if (limit != null) {
                    progress_horizontal.max = limit
                }
            }
        }.start()
    }

    fun updateImages(launches : ArrayList<Launch>){
        Thread{
            this.runOnUiThread {
                currentLaunches = launches
                adapter.notifyDataSetChanged()
            }
        }

    }
}