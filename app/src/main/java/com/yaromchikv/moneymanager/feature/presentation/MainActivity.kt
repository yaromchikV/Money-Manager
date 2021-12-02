package com.yaromchikv.moneymanager.feature.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.core.view.forEach
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.databinding.ActivityMainBinding
import com.yaromchikv.moneymanager.feature.presentation.ui.accounts.AccountsFragmentDirections
import com.yaromchikv.moneymanager.feature.presentation.ui.chart.ChartFragmentDirections
import com.yaromchikv.moneymanager.feature.presentation.ui.transactions.TransactionsFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding(R.id.container)

    private val viewModel: MainActivityViewModel by viewModels()

    private val navController: NavController by lazy {
        (supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment).navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        var currentDestination: Int? = null

        binding.buttonSettings.setOnClickListener {
            viewModel.settingsButtonClick()
        }

        binding.toolbarInfoBox.setOnClickListener {
            viewModel.selectAccountButtonClick()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is MainActivityViewModel.Event.OpenTheSettingsScreen -> {
                        when (currentDestination) {
                            R.id.accounts_fragment -> navController.navigate(
                                AccountsFragmentDirections.actionAccountsFragmentToSettingsFragment()
                            )
                            R.id.transactions_fragment -> navController.navigate(
                                TransactionsFragmentDirections.actionTransactionsFragmentToSettingsFragment()
                            )
                            R.id.chart_fragment -> navController.navigate(
                                ChartFragmentDirections.actionChartFragmentToSettingsFragment()
                            )
                        }
                    }
                    is MainActivityViewModel.Event.OpenTheSelectAccountDialog -> {
                        navController.navigate(R.id.action_dialog_fragment_account_filter)
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.currentAccount.collectLatest {
                binding.toolbarTitle.text = it?.name ?: getString(R.string.all_accounts)
            }
        }

        val pattern = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        lifecycleScope.launchWhenStarted {
            viewModel.currentDateRange.collectLatest {
                binding.toolbarSubtitle.text =
                    if (it.first == null && it.second == null)
                        getString(R.string.all_time)
                    else if (it.first == it.second)
                        it.first?.format(pattern)
                    else
                        "${it.first?.format(pattern)} - ${it.second?.format(pattern)}"
            }
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.accounts_fragment, R.id.transactions_fragment, R.id.chart_fragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentDestination = destination.id

            val isFragmentWithoutSettings = when (currentDestination) {
                R.id.settings_fragment -> true
                R.id.account_add_fragment -> true
                R.id.account_edit_fragment -> true
                else -> false
            }

            binding.bottomNavigation.visibility =
                if (isFragmentWithoutSettings) View.GONE else View.VISIBLE
            binding.buttonSettings.visibility =
                if (isFragmentWithoutSettings) View.GONE else View.VISIBLE
            binding.toolbarInfoBox.visibility =
                if (isFragmentWithoutSettings) View.GONE else View.VISIBLE
            supportActionBar?.setDisplayShowTitleEnabled(isFragmentWithoutSettings)

            val isAccountsFragments = when (currentDestination) {
                R.id.accounts_fragment -> true
                R.id.account_actions_sheet_fragment -> true
                else -> false
            }

            binding.moreButton.visibility = if (isAccountsFragments) View.GONE else View.VISIBLE
            binding.toolbarInfoBox.isEnabled = !isAccountsFragments
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}