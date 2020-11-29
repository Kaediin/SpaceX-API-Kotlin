package com.kaedin.api

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaedin.api.models.Filter
import com.kaedin.api.models.Launch
import com.google.android.material.snackbar.Snackbar
import com.kaedin.api.adapters.AdapterLaunch
import com.kaedin.api.asynctasks.ApiRequest
import com.kaedin.api.utils.DataUtils
import com.kaedin.api.utils.SimpleDividerItemDecoration
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()

    private lateinit var spUpcoming: SharedPreferences
    private lateinit var spReverse: SharedPreferences

    private lateinit var adapter: RecyclerView.Adapter<*>

    private var filter: Filter = Filter()

    var currentLaunches = ArrayList<Launch>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        spUpcoming = getSharedPreferences("show_upcoming", Context.MODE_PRIVATE)
        spReverse = getSharedPreferences("reverse_list", Context.MODE_PRIVATE)

        val showUpcoming = spUpcoming.getBoolean("show_upcoming", true)
        val reverseList = spReverse.getBoolean("reverse_list", false)

        filter.showUpcoming = showUpcoming
        filter.reverseList = reverseList

        run()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val switchUpcoming: MenuItem = menu!!.findItem(R.id.switch_upcoming)
        if (filter.showUpcoming) {
            switchUpcoming.title = "Hide upcoming"
        } else {
            switchUpcoming.title = "Show upcoming"
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

                if (filter.showUpcoming) {
                    item.title = "Show upcoming"
                    makeSnackbar("Hiding upcoming launches", Snackbar.LENGTH_SHORT)
                } else {
                    item.title = "Hide upcoming"
                    makeSnackbar("Showing upcoming launches", Snackbar.LENGTH_SHORT)
                }

                filter.showUpcoming = !filter.showUpcoming
                spUpcoming.edit().putBoolean("show_upcoming", filter.showUpcoming).apply()

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
                spReverse.edit().putBoolean("reverse_list", filter.reverseList).apply()

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

    private fun run() {
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
                recyclerView.addItemDecoration(SimpleDividerItemDecoration(this))
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
}