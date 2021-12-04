package com.yaromchikv.moneymanager.feature.presentation.ui.transactions

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.databinding.FragmentTransactionsBinding
import com.yaromchikv.moneymanager.feature.domain.model.TransactionView
import com.yaromchikv.moneymanager.feature.presentation.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class TransactionsFragment : Fragment(R.layout.fragment_transactions) {

    private val binding: FragmentTransactionsBinding by viewBinding()

    private val viewModel: TransactionsViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    @Inject
    lateinit var transactionAdapter: TransactionsRVAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        binding.listOfTransactions.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
            )
        }

        binding.newTransactionButton.setOnClickListener {
            viewModel.addTransactionClick(
                activityViewModel.currentAccount.value ?: activityViewModel.accounts.value[0]
            )
        }

        transactionAdapter.setOnDeleteClickListener(TransactionsRVAdapter.OnDeleteClickListener {
            viewModel.deleteButtonClick(it)
        })

        lifecycleScope.launchWhenStarted {
            viewModel.transactionsWithDayInfo.collectLatest {
                transactionAdapter.submitList(it)

                binding.noTransactionImage.visibility =
                    if (it.isEmpty()) View.VISIBLE else View.INVISIBLE
                binding.noTransactionText.visibility =
                    if (it.isEmpty()) View.VISIBLE else View.INVISIBLE
            }
        }

        lifecycleScope.launchWhenStarted {
            activityViewModel.currentDateRange.collectLatest {
                viewModel.setDateRange(it.first, it.second)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is TransactionsViewModel.Event.OpenTheAddTransactionSheet -> {
                        if (getCurrentDestination() == this@TransactionsFragment.javaClass.name) {
                            findNavController().navigate(
                                TransactionsFragmentDirections.actionTransactionsFragmentToSelectCategorySheetFragment(
                                    it.account
                                )
                            )
                        }
                    }
                    is TransactionsViewModel.Event.SelectDate -> {
                        if (getCurrentDestination() == this@TransactionsFragment.javaClass.name) {
                            findNavController().navigate(
                                TransactionsFragmentDirections.actionTransactionsFragmentToSelectDateDialogFragment()
                            )
                        }
                    }
                    is TransactionsViewModel.Event.ShowTheDeleteTransactionDialog -> {
                        if (!alertIsShowing) {
                            showAlertDialog(it.transaction)
                        }
                    }
                    is TransactionsViewModel.Event.DeleteTransaction -> {
                        viewModel.deleteTransaction(it.transaction)
                    }
                }
            }
        }
    }

    private var alertIsShowing = false

    private fun showAlertDialog(transaction: TransactionView) {
        alertIsShowing = true

        val alert = AlertDialog.Builder(requireContext())
            .setIcon(R.drawable.ic_warning)
            .setTitle("Do you really to delete this transaction? ")
            .setMessage("From: ${transaction.categoryName}\nTo: ${transaction.accountName}\nAmount: ${transaction.amount}")
            .setPositiveButton(getString(R.string.ok)) { _, _ ->
                viewModel.deleteConfirmationButtonClick(transaction)
                transactionAdapter.clearSelectedPosition()
                alertIsShowing = false
            }
            .setNegativeButton(getString(R.string.cancel)) { _, _ ->
                alertIsShowing = false
            }
            .setOnCancelListener { alertIsShowing = false }
            .create()
        alert.show()
    }

    override fun onPause() {
        super.onPause()
        transactionAdapter.clearSelectedPosition()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.date_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.select_date) {
            viewModel.selectDateClick()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getCurrentDestination() =
        (findNavController().currentDestination as? FragmentNavigator.Destination)?.className
            ?: (findNavController().currentDestination as? DialogFragmentNavigator.Destination)?.className

}