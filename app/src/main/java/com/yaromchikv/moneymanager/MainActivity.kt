package com.yaromchikv.moneymanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yaromchikv.moneymanager.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding(R.id.container)

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.bottomNavigation
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_categories,
                R.id.navigation_accounts,
                R.id.navigation_transactions
            )
        )

        setupActionBarWithNavController(navHostFragment.navController, appBarConfiguration)
        navView.setupWithNavController(navHostFragment.navController)
    }
}