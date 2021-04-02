package com.kaedin.api.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kaedin.api.fragments.LandpadDetailsFragment
import com.kaedin.api.fragments.LaunchDetailsFragment
import com.kaedin.api.fragments.LaunchpadDetailsFragment
import com.kaedin.api.fragments.RocketDetailsFragment

class PagerAdapterDetails(fm : FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> LaunchDetailsFragment()

            1 -> RocketDetailsFragment()

            else -> LaunchpadDetailsFragment()
        }
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> "Launch"

            1 -> "Rocket"

            else -> "Launchpad"
        }
    }
}