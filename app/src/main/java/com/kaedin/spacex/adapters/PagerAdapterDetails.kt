package com.kaedin.spacex.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kaedin.spacex.activities.CoreDetailActivity
import com.kaedin.spacex.fragments.*

class PagerAdapterDetails(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> LaunchDetailsFragment()

            1 -> RocketDetailsFragment()

            2 -> LaunchpadDetailsFragment()

            3 -> PayloadDetailsFragment()

             4 -> ShipDetailsFragment()

            5 -> CapsuleDetailsFragment()

            else -> CoreDetailsFragment()
        }
    }

    override fun getCount(): Int {
        return 7
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Launch"

            1 -> "Rocket"

            2 -> "Launchpad"

            3 -> "Payload"

            4 -> "Ship"

            5 -> "Capsule"

            else -> "Cores"
        }
    }
}