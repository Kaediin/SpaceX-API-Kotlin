package com.kaedin.spacex.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kaedin.spacex.fragments.*

class PagerAdapterDetails(fm: FragmentManager, validTabs : HashMap<String, Any>) : FragmentPagerAdapter(fm) {

    private val tabs : HashMap<String, Any> = validTabs

    override fun getItem(position: Int): Fragment {
        return getValidPages()[position]
    }

    override fun getCount(): Int {
        return getValidPages().size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tabs.keys.toList()[(tabs.keys.size-1) - position]
    }

    fun getValidPages(): ArrayList<Fragment> {
        val fragments = ArrayList<Fragment>()
        for ((key, value) in tabs) {
            when (key){
                "Launch" -> fragments.add(LaunchDetailsFragment(value.toString()))
                "Rocket" -> fragments.add(RocketDetailsFragment(value.toString()))
                "Launchpad" -> fragments.add(LaunchpadDetailsFragment())
                "Payload" -> fragments.add(PayloadDetailsFragment())
                "Ship" -> fragments.add(ShipDetailsFragment())
                "Capsule" -> fragments.add(CapsuleDetailsFragment())
                "Cores" -> fragments.add(CoreDetailsFragment())
                "Crew" -> fragments.add(CrewDetailsFragment())
            }
        }
        return fragments.reversed() as ArrayList<Fragment>
    }
}