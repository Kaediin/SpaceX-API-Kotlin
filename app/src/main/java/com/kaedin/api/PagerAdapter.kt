package com.kaedin.api

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kaedin.api.fragments.launchInfoFragment
import com.kaedin.api.fragments.rocketFragment

class PagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position){
            0 -> launchInfoFragment()

            else -> rocketFragment()
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position){
            0 -> "Launch info"

            else -> "Rocket info"
        }
    }
}