package com.kaedin.spacex.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kaedin.spacex.fragments.*

class PagerAdapterHome(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> LaunchesFragment()

            1 -> RocketsFragment()

            2 -> DragonsFragment()

            3 -> LandpadsFragment()

            4 -> LaunchpadsFragment()

            5 -> PayloadFragment()

            6 -> ShipsFragment()

            7 -> CapsulesFragment()

            8 -> CoresFragment()

            else -> CrewFragment()
        }
    }

    override fun getCount(): Int {
        return 10
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Launches"

            1 -> "Rockets"

            2 -> "Dragons"

            3 -> "Landpads"

            4 -> "Launchpads"

            5 -> "Payloads"

            6 -> "Ships"

            7 -> "Capsules"

            8 -> "Cores"

            else -> "Crew"
        }
    }
}