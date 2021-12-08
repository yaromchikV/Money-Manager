package com.yaromchikv.moneymanager.feature.presentation.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.THEME_DARK
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.THEME_LIGHT
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.THEME_PREFERENCE_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        findPreference<ListPreference>(THEME_PREFERENCE_KEY)?.setOnPreferenceChangeListener { _, newValue ->
            AppCompatDelegate.setDefaultNightMode(
                when (newValue) {
                    THEME_LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                    THEME_DARK -> AppCompatDelegate.MODE_NIGHT_YES
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            )
            true
        }
    }
}
