package com.yaromchikv.moneymanager.feature.presentation.ui.accounts.actions

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.common.DateUtils.toAmountFormat
import com.yaromchikv.moneymanager.databinding.SheetFragmentAccountActionsBinding
import com.yaromchikv.moneymanager.feature.presentation.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AccountActionsSheetFragment : BottomSheetDialogFragment() {

    private var _binding: SheetFragmentAccountActionsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AccountActionsViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    private val args by navArgs<AccountActionsSheetFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SheetFragmentAccountActionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupContainerAppearance()
        setupDeleteButtonVisibility()
        setupOnClickListeners()
        setupEventCollector()
    }

    private fun setupContainerAppearance() {
        val account = args.selectedAccount
        with(binding) {
            actionsContainer.setBackgroundColor(Color.parseColor(account.color))
            accountName.text = account.name
            accountAmount.text = account.amount.toAmountFormat(withMinus = false)
            accountCurrency.text = activityViewModel.getCurrency()
        }
    }

    private fun setupDeleteButtonVisibility() {
        val size = args.numberOfAccounts
        with(binding) {
            deleteButton.visibility = if (size == 1) View.GONE else View.VISIBLE
            deleteButtonText.visibility = if (size == 1) View.INVISIBLE else View.VISIBLE
            deleteButtonIcon.visibility = if (size == 1) View.INVISIBLE else View.VISIBLE
        }
    }

    private fun setupOnClickListeners() {
        with(binding) {
            editButton.setOnClickListener { viewModel.editButtonClick(args.selectedAccount) }
            deleteButton.setOnClickListener { viewModel.deleteButtonClick(args.selectedAccount) }
        }
    }

    private fun setupEventCollector() {
        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is AccountActionsViewModel.Event.NavigateToEditAccountScreen -> {
                        findNavController().navigate(
                            AccountActionsSheetFragmentDirections
                                .actionAccountActionsSheetFragmentToAccountEditFragment(args.selectedAccount)
                        )
                    }
                    is AccountActionsViewModel.Event.ShowTheDeleteAccountDialog -> {
                        getAlertDialog().show()
                    }
                    is AccountActionsViewModel.Event.DeleteAccount -> {
                        if (args.selectedAccount == activityViewModel.selectedAccount.value)
                            activityViewModel.setCurrentAccount(null)

                        viewModel.deleteAccount(args.selectedAccount)
                        dismiss()
                    }
                }
            }
        }
    }

    private fun getAlertDialog() = AlertDialog.Builder(requireContext())
        .setIcon(R.drawable.ic_warning)
        .setTitle(getString(R.string.delete_alert_title))
        .setMessage(getString(R.string.delete_alert_subtitle))
        .setPositiveButton(getString(R.string.ok)) { _, _ ->
            viewModel.deleteConfirmationButtonClick()
            this@AccountActionsSheetFragment.dismiss()
        }
        .setNegativeButton(getString(R.string.cancel)) { _, _ ->
            this@AccountActionsSheetFragment.dismiss()
        }
        .setCancelable(false)
        .create()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
