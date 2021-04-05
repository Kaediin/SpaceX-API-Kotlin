package com.kaedin.spacex.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kaedin.spacex.R
import com.kaedin.spacex.adapters.PagerAdapterDetails
import kotlinx.android.synthetic.main.activity_detail.*

class LaunchDetailActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar!!.hide()

        val validTabs: HashMap<String, Any> = getDictionaryValidTabs()
        val pagerAdapter = PagerAdapterDetails(supportFragmentManager, validTabs)
        viewpager_main_details.offscreenPageLimit = validTabs.size
        viewpager_main_details.adapter = pagerAdapter

        tabs_main_details.setupWithViewPager(viewpager_main_details)
        tabs_main_details.tabTextColors = resources.getColorStateList(android.R.color.white)
    }

    fun getDictionaryValidTabs(): HashMap<String, Any> {
        val hashMap = HashMap<String, Any>()
        val launchId = intent.getStringExtra("launch_id")
        val rocketId = intent.getStringExtra("rocket_id")
        val launchpadId = intent.getStringExtra("launchpad_id")
        val payloadIds = intent.getStringArrayListExtra("payload_ids")
        val shipIds = intent.getStringArrayListExtra("ship_ids")
        val capsuleIds = intent.getStringArrayListExtra("capsule_ids")
        val coreIds = intent.getStringArrayListExtra("core_ids")
        val crewIds = intent.getStringArrayListExtra("crew_ids")

        if (launchId != null && launchId.isNotBlank()) hashMap["Launch"] = launchId
        if (rocketId != null && rocketId.isNotBlank()) hashMap["Rocket"] = rocketId
        if (launchpadId != null && launchpadId.isNotBlank()) hashMap["Launchpad"] = launchpadId
        if (payloadIds != null && payloadIds.isNotEmpty()) hashMap["Payload"] = payloadIds
        if (shipIds != null && shipIds.isNotEmpty()) hashMap["Ship"] = shipIds
        if (capsuleIds != null && capsuleIds.isNotEmpty()) hashMap["Capsule"] = capsuleIds
        if (coreIds != null && coreIds.isNotEmpty()) hashMap["Cores"] = coreIds
        if (crewIds != null && crewIds.isNotEmpty()) hashMap["Crew"] = crewIds
        return hashMap
    }

}