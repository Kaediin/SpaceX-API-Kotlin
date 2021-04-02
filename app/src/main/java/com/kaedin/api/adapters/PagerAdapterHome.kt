package com.kaedin.api.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kaedin.api.fragments.*

class PagerAdapterHome(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> LaunchesFragment()

            1 -> RocketsFragment()

            2 -> DragonsFragment()

            3 -> LandpadsFragment()

            else -> LaunchpadsFragment()
        }
    }

    override fun getCount(): Int {
        return 5
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Launches"

            1 -> "Rockets"

            2 -> "Dragons"

            3 -> "Landpads"

            else -> "Launchpads"
        }
    }
}