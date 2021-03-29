package com.kaedin.api.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kaedin.api.fragments.LaunchDetailsFragment
import com.kaedin.api.fragments.RocketDetailsFragment

class PagerAdapterDetails(fm : FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> LaunchDetailsFragment()

            else -> RocketDetailsFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> "Launch"

            else -> "Rocket"
        }
    }
}