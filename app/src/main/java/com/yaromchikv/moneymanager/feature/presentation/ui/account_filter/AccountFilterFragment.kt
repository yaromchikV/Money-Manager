package com.yaromchikv.moneymanager.feature.presentation.ui.account_filter

import android.os.Bundle
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.databinding.DialogFragmentAccountFilterBinding
import com.yaromchikv.moneymanager.feature.presentation.MainActivityViewModel
import com.yaromchikv.moneymanager.feature.presentation.ui.accounts.AccountsRVAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class AccountFilterFragment : DialogFragment(R.layout.dialog_fragment_account_filter) {

    private val binding: DialogFragmentAccountFilterBinding by viewBinding()

    private val viewModel: AccountFilterViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    @Inject
    lateinit var accountsAdapter: AccountsRVAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        accountsAdapter.setOnClickListener(AccountsRVAdapter.OnClickListener {
            viewModel.selectAccount(it)
            dismiss()
        })

        binding.listOfAccounts.apply {
            adapter = accountsAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(getDivider())
        }

        lifecycleScope.launchWhenStarted {
            viewModel.accounts.collectLatest { newList ->
                accountsAdapter.submitList(newList)
            }
        }

        binding.allAccountsItem.setOnClickListener {
            activityViewModel.setCurrentAccount(null)
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
}