package com.yaromchikv.moneymanager.ui.accounts

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.databinding.FragmentAccountsBinding

class AccountsFragment : Fragment(R.layout.fragment_accounts) {

    private val binding: FragmentAccountsBinding by viewBinding()

    private val accountsViewModel: AccountsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView: TextView = binding.textAccounts
        accountsViewModel.text.observe(viewLifecycleOwner, {
            textView.text = it
        })
    }
}