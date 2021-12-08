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
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.CURRENCY_PREFERENCE_KEY
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

        val account = args.selectedAccount
        val size = args.numberOfAccounts

        binding.accountName.text = account.name
        binding.accountAmount.text = account.amount.toAmountFormat(withMinus = false)
        binding.accountCurrency.text = activityViewModel.getCurrency()

        binding.actionsContainer.setBackgroundColor(Color.parseColor(account.color))

        binding.deleteButton.visibility = if (size == 1) View.GONE else View.VISIBLE
        binding.deleteButtonText.visibility = if (size == 1) View.INVISIBLE else View.VISIBLE
        binding.deleteButtonIcon.visibility = if (size == 1) View.INVISIBLE else View.VISIBLE

        binding.editButton.setOnClickListener {
            viewModel.editButtonClick(account)
        }
        binding.deleteButton.setOnClickListener {
            viewModel.deleteButtonClick(account)
        }

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is AccountActionsViewModel.Event.NavigateToEditAccountScreen -> {
                        findNavController().navigate(
                            AccountActionsSheetFragmentDirections
                                .actionAccountActionsSheetFragmentToAccountEditFragment(account)
                        )
                    }
                    is AccountActionsViewModel.Event.ShowTheDeleteAccountDialog -> {
                        val alert = AlertDialog.Builder(requireContext())
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
                        alert.show()
                    }
                    is AccountActionsViewModel.Event.DeleteAccount -> {
                        if (account == activityViewModel.selectedAccount.value) {
                            activityViewModel.setCurrentAccount(null)
                        }
                        viewModel.deleteAccount(account)
                        dismiss()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}