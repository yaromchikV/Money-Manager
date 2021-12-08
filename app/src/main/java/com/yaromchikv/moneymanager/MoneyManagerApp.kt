package com.yaromchikv.moneymanager

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.THEME_DARK
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.THEME_DEFAULT
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.THEME_LIGHT
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.THEME_PREFERENCE_KEY
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MoneyManagerApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val theme = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            .getString(THEME_PREFERENCE_KEY, THEME_DEFAULT)

        AppCompatDelegate.setDefaultNightMode(
            when (theme) {
                THEME_LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                THEME_DARK -> AppCompatDelegate.MODE_NIGHT_YES
                else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        )
    }
}