package com.kaedin.api.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kaedin.api.fragments.LaunchesFragment
import com.kaedin.api.fragments.RocketsFragment

class PagerAdapterHome(fm : FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> LaunchesFragment()

            else -> RocketsFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> "Launches"

            else -> "Rockets"
        }
    }
}