package com.yaromchikv.moneymanager.feature.presentation.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.feature.presentation.ui.auth.AuthActivity
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.FIRST_TIME_PREFERENCE_KEY
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.RESET_PIN_PREFERENCE_KEY
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.THEME_DARK
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.THEME_LIGHT
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.THEME_PREFERENCE_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat() {

    private var counter = 0

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

        findPreference<Preference>(RESET_PIN_PREFERENCE_KEY)?.setOnPreferenceClickListener { preference ->
            preference.summary = getString(R.string.reset_pin_message_repeat)
            counter++
            if (counter > 1) {
                preferenceManager.sharedPreferences
                    .edit()
                    .putBoolean(FIRST_TIME_PREFERENCE_KEY, true)
                    .apply()

                val intent = Intent(activity, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            false
        }
    }
}
