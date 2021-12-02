package com.yaromchikv.moneymanager.feature.presentation.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.yaromchikv.moneymanager.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}