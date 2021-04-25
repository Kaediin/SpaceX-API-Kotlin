package com.kaedin.spacex.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragment
import com.kaedin.spacex.R

class SettingsFragment : PreferenceFragment() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }
}