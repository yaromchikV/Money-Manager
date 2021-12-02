package com.yaromchikv.moneymanager.feature.presentation.ui.accounts

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.common.toAmountFormat
import com.yaromchikv.moneymanager.databinding.FragmentAccountsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class AccountsFragment : Fragment(R.layout.fragment_accounts) {

    private val binding: FragmentAccountsBinding by viewBinding()

    private val viewModel: AccountsViewModel by viewModels()

    @Inject
    lateinit var accountsAdapter: AccountsRVAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        accountsAdapter.setOnClickListener(AccountsRVAdapter.OnClickListener {
            viewModel.selectAccount(it)
        })

        binding.listOfAccounts.apply {
            adapter = accountsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(getDivider())
        }

        lifecycleScope.launchWhenStarted {
            viewModel.accounts.collectLatest { newList ->
                accountsAdapter.submitList(newList)
                var amount = 0.0
                newList.forEach { amount += it.amount }

                binding.fullAmount.text = amount.toAmountFormat(withMinus = false)
                binding.mainCurrency.text = viewModel.getPreferences().getString(
                    "currency",
                    requireContext().resources.getStringArray(R.array.currency_values)[0]
                )
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is AccountsViewModel.Event.NavigateToAddAccountScreen -> {
                        if (getCurrentDestination() == this@AccountsFragment.javaClass.name) {
                            findNavController().navigate(AccountsFragmentDirections.actionAccountsFragmentToAccountAddFragment())
                        }
                    }
                    is AccountsViewModel.Event.OpenTheAccountActionsSheet -> {
                        if (getCurrentDestination() == this@AccountsFragment.javaClass.name) {
                            findNavController().navigate(
                                AccountsFragmentDirections.actionAccountsFragmentToAccountActionsSheetFragment(
                                    it.account
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.accounts_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.add_account) {
            viewModel.addButtonClick()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getDivider() = DividerItemDecoration(
        requireContext(),
        DividerItemDecoration.VERTICAL
    ).apply {
        setDrawable(
            requireNotNull(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.divider_layer,
                    null
                )
            )
        )
    }

    private fun getCurrentDestination() =
        (findNavController().currentDestination as? FragmentNavigator.Destination)?.className
            ?: (findNavController().currentDestination as? DialogFragmentNavigator.Destination)?.className
}