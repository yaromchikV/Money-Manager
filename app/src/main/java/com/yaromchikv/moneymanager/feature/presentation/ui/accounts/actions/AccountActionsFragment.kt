package com.yaromchikv.moneymanager.feature.presentation.ui.accounts.actions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yaromchikv.moneymanager.databinding.AccountActionsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountActionsFragment : BottomSheetDialogFragment() {

    private var _binding: AccountActionsFragmentBinding? = null
    private val binding = requireNotNull(_binding)

    private val viewModel: AccountActionsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AccountActionsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TODO
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}