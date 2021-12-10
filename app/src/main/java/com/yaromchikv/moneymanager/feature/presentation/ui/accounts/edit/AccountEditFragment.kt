package com.yaromchikv.moneymanager.feature.presentation.ui.accounts.edit

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.common.DateUtils.toAmountFormat
import com.yaromchikv.moneymanager.databinding.FragmentAccountEditBinding
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.setTint
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AccountEditFragment : Fragment(R.layout.fragment_account_edit) {

    private val binding: FragmentAccountEditBinding by viewBinding()

    private val viewModel: AccountEditViewModel by viewModels()

    private val args by navArgs<AccountEditFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupOnClickListeners()
        setupFields()
        setupEventCollector()
    }

    private fun setupFields() {
        val account = args.editableAccount

        with(binding) {
            nameTextField.editText?.setText(account.name)
            amountTextField.editText?.setText(account.amount.toAmountFormat(withMinus = false))
            selectedColor.setTint(account.color)
        }
    }

    private fun setupEventCollector() {
        val account = args.editableAccount
        var color = account.color

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is AccountEditViewModel.Event.UpdateAccount -> {
                        val name = binding.nameTextField.editText?.text.toString().trim()
                        if (name.isEmpty()) {
                            showToast(context, getString(R.string.account_empty_name_error))
                        } else {
                            val amount = binding.amountTextField.editText?.text.toString()
                                .toDoubleOrNull() ?: account.amount

                            val newAccount =
                                account.copy(name = name, amount = amount, color = color)

                            viewModel.updateAccount(newAccount)
                            if (getCurrentDestination() == this@AccountEditFragment.javaClass.name)
                                findNavController().navigate(AccountEditFragmentDirections.actionAccountEditFragmentToAccountsFragment())
                        }
                    }
                    is AccountEditViewModel.Event.SelectColor -> {
                        binding.selectedColor.setTint(it.color)
                        color = it.color
                    }
                }
            }
        }
    }

    private fun setupOnClickListeners() {
        with(binding) {
            color0.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
            color1.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
            color2.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
            color3.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
            color4.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
            color5.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
            color6.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
            color7.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
            color8.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
            color9.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
            color10.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
            color11.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
            color12.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
            color13.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
            color14.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
            color15.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.apply) {
            viewModel.applyButtonClick()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getCurrentDestination() =
        (findNavController().currentDestination as? FragmentNavigator.Destination)?.className
            ?: (findNavController().currentDestination as? DialogFragmentNavigator.Destination)?.className
}
