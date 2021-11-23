package com.yaromchikv.moneymanager.feature.presentation.ui.accounts.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.databinding.AccountEditFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountEditFragment : Fragment(R.layout.account_edit_fragment) {

    private val binding: AccountEditFragmentBinding by viewBinding()

    private val viewModel: AccountEditViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}