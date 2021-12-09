package com.yaromchikv.moneymanager.feature.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.common.DateUtils.getCurrentLocalDate
import com.yaromchikv.moneymanager.databinding.ActivityMainBinding
import com.yaromchikv.moneymanager.feature.presentation.ui.accounts.AccountsFragmentDirections
import com.yaromchikv.moneymanager.feature.presentation.ui.chart.ChartFragmentDirections
import com.yaromchikv.moneymanager.feature.presentation.ui.converter.CurrencyConverterFragmentDirections
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
                            R.id.currency_converter_fragment -> navController.navigate(
                                CurrencyConverterFragmentDirections.actionCurrencyConverterFragmentToSettingsActivity()
                            )
                            R.id.accounts_fragment -> navController.navigate(
                                AccountsFragmentDirections.actionAccountsFragmentToSettingsActivity()
                            )
                            R.id.transactions_fragment -> navController.navigate(
                                TransactionsFragmentDirections.actionTransactionsFragmentToSettingsActivity()
                            )
                            R.id.chart_fragment -> navController.navigate(
                                ChartFragmentDirections.actionChartFragmentToSettingsActivity()
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
            viewModel.selectedAccount.collectLatest {
                binding.bankIcon.visibility = if (it != null) View.VISIBLE else View.INVISIBLE
                binding.toolbarTitle.text = it?.name ?: getString(R.string.all_accounts)
            }
        }

        val pattern = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        lifecycleScope.launchWhenStarted {
            viewModel.selectedDateRange.collectLatest {
                binding.toolbarSubtitle.text =
                    if (it.first == null && it.second == null)
                        getString(R.string.all_time)
                    else if (it.second == null)
                        "${it.first?.format(pattern)} - ${getCurrentLocalDate().format(pattern)}"
                    else
                        it.first?.format(pattern)
            }
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.currency_converter_fragment,
                R.id.accounts_fragment,
                R.id.transactions_fragment,
                R.id.chart_fragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentDestination = destination.id

            val isFragmentWithoutSettingsButton = when (currentDestination) {
                R.id.account_add_fragment -> true
                R.id.account_edit_fragment -> true
                else -> false
            }

            binding.bottomNavigation.visibility =
                if (isFragmentWithoutSettingsButton) View.GONE else View.VISIBLE
            binding.buttonSettings.visibility =
                if (isFragmentWithoutSettingsButton) View.GONE else View.VISIBLE

            binding.toolbarInfoBox.visibility =
                if (isFragmentWithoutSettingsButton) View.GONE else View.VISIBLE

            supportActionBar?.setDisplayShowTitleEnabled(isFragmentWithoutSettingsButton)

            val isFragmentWithoutAccountsFilter = when (currentDestination) {
                R.id.currency_converter_fragment -> true
                R.id.accounts_fragment -> true
                R.id.account_actions_sheet_fragment -> true
                else -> false
            }

            binding.moreButton.visibility =
                if (isFragmentWithoutAccountsFilter) View.INVISIBLE else View.VISIBLE
            binding.toolbarInfoBox.isEnabled = !isFragmentWithoutAccountsFilter
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}