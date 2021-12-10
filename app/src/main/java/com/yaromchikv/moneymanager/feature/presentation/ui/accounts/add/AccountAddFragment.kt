package com.yaromchikv.moneymanager.feature.presentation.ui.accounts.add

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
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.databinding.FragmentAccountAddBinding
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.MAIN_COLOR
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.setTint
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AccountAddFragment : Fragment(R.layout.fragment_account_add) {

    private val binding: FragmentAccountAddBinding by viewBinding()

    private val viewModel: AccountAddViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        setupOnClickListeners()
        setupEventCollector()
    }

    private fun setupEventCollector() {
        var color = MAIN_COLOR

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is AccountAddViewModel.Event.AddAccount -> {
                        val name = binding.nameTextField.editText?.text.toString().trim()
                        if (name.isEmpty()) {
                            showToast(requireContext(), getString(R.string.account_empty_name_error))
                        } else {
                            val amount = binding.amountTextField.editText?.text.toString()
                                .toDoubleOrNull() ?: 0.0

                            val account = Account(name = name, amount = amount, color = color)
                            viewModel.addAccount(account)

                            if (getCurrentDestination() == this@AccountAddFragment.javaClass.name)
                                findNavController().navigate(AccountAddFragmentDirections.actionAccountAddFragmentToAccountsFragment())
                        }
                    }
                    is AccountAddViewModel.Event.SelectColor -> {
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
