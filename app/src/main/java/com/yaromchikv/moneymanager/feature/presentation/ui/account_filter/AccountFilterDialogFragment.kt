package com.yaromchikv.moneymanager.feature.presentation.ui.account_filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.common.DateUtils.toAmountFormat
import com.yaromchikv.moneymanager.databinding.DialogFragmentAccountFilterBinding
import com.yaromchikv.moneymanager.feature.presentation.MainActivityViewModel
import com.yaromchikv.moneymanager.feature.presentation.ui.accounts.AccountsRVAdapter
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.CURRENCY_PREFERENCE_KEY
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.getDivider
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.setTint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class AccountFilterDialogFragment : DialogFragment(R.layout.dialog_fragment_account_filter) {

    private val binding: DialogFragmentAccountFilterBinding by viewBinding()

    private val viewModel: AccountFilterViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    @Inject
    lateinit var accountsAdapter: AccountsRVAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.listOfAccounts.apply {
            adapter = accountsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(getDivider(context))
        }

        binding.allAccountsCurrency.text = viewModel.getPreferences().getString(
            CURRENCY_PREFERENCE_KEY,
            requireContext().resources.getStringArray(R.array.currency_values)[0]
        )

        binding.allAccountsIconColor.setTint(activityViewModel.selectedAccount.value?.color)


        binding.allAccountsItem.setOnClickListener {
            activityViewModel.setCurrentAccount(null)
            dismiss()
        }

        accountsAdapter.setOnClickListener(AccountsRVAdapter.OnClickListener {
            viewModel.selectAccount(it)
            dismiss()
        })

        lifecycleScope.launchWhenStarted {
            viewModel.accounts.collectLatest { newList ->
                accountsAdapter.submitList(newList)
                binding.allAccountsAmount.text =
                    viewModel.getFullAmount().toAmountFormat(withMinus = false)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is AccountFilterViewModel.Event.SelectAccount -> {
                        activityViewModel.setCurrentAccount(it.account)
                    }
                }
            }
        }

    }
}