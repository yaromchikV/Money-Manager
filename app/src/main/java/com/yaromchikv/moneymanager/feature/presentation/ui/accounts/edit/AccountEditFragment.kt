package com.yaromchikv.moneymanager.feature.presentation.ui.accounts.edit

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.databinding.AccountEditFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AccountEditFragment : Fragment(R.layout.account_edit_fragment) {

    private val binding: AccountEditFragmentBinding by viewBinding()

    private val viewModel: AccountEditViewModel by viewModels()

    private val args by navArgs<AccountEditFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        val account = args.editableAccount

        binding.nameTextField.editText?.setText(account.name)
        binding.amountTextField.editText?.setText(account.amount.toString())
        binding.currencyTextField.editText?.setText(account.currency)

//        val color = ResourcesCompat.getColor(resources, account.color, null)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            binding.colorPicker.background.colorFilter =
//                BlendModeColorFilter(color, BlendMode.SRC_ATOP)
//        } else {
//            binding.colorPicker.background.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
//        }

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is AccountEditViewModel.Event.UpdateAccount -> {
                        viewModel.updateAccount(account)
                        findNavController().navigate(AccountEditFragmentDirections.actionAccountEditFragmentToAccountsFragment())
                    }
                }
            }
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
}