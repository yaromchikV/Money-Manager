package com.yaromchikv.moneymanager.feature.presentation.ui.accounts.add

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.databinding.AccountAddFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountAddFragment : Fragment(R.layout.account_add_fragment) {

    private val binding: AccountAddFragmentBinding by viewBinding()

    private val viewModel: AccountAddViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}