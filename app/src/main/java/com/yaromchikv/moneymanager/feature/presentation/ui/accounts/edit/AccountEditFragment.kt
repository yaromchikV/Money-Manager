package com.yaromchikv.moneymanager.feature.presentation.ui.accounts.edit

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.databinding.FragmentAccountEditBinding
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

        colorClickListener()

        val account = args.editableAccount

        binding.nameTextField.editText?.setText(account.name)
        binding.amountTextField.editText?.setText(account.amount.toString())

        var color = account.color

        DrawableCompat.setTint(
            binding.selectedColor.drawable,
            Color.parseColor(color)
        )

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is AccountEditViewModel.Event.UpdateAccount -> {
                        val name = binding.nameTextField.editText?.text.toString().trim()
                        if (name.isEmpty()) {
                            Toast.makeText(
                                context,
                                "The field with the account name is empty",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            val newAccount = account.copy(
                                name = name,
                                amount = binding.amountTextField.editText?.text.toString()
                                    .toDoubleOrNull() ?: account.amount,
                                color = color
                            )

                            viewModel.updateAccount(newAccount)
                            if (getCurrentDestination() == this@AccountEditFragment.javaClass.name)
                                findNavController().navigate(AccountEditFragmentDirections.actionAccountEditFragmentToAccountsFragment())
                        }
                    }
                    is AccountEditViewModel.Event.SelectColor -> {
                        DrawableCompat.setTint(
                            binding.selectedColor.drawable,
                            Color.parseColor(it.color)
                        )
                        color = it.color
                    }
                }
            }
        }
    }

    private fun colorClickListener() {
        binding.color0.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
        binding.color1.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
        binding.color2.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
        binding.color3.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
        binding.color4.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
        binding.color5.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
        binding.color6.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
        binding.color7.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
        binding.color8.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
        binding.color9.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
        binding.color10.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
        binding.color11.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
        binding.color12.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
        binding.color13.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
        binding.color14.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
        binding.color15.setOnClickListener { viewModel.selectColorClick(it as ImageView) }
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