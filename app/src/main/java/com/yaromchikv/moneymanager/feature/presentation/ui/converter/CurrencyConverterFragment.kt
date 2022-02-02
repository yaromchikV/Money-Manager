package com.yaromchikv.moneymanager.feature.presentation.ui.converter

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.databinding.FragmentCurrencyConverterBinding
import com.yaromchikv.moneymanager.feature.presentation.MainActivityViewModel
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CurrencyConverterFragment : Fragment(R.layout.fragment_currency_converter) {

    private val binding: FragmentCurrencyConverterBinding by viewBinding()

    private val viewModel: CurrencyConverterViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupConversionCollector()
        setupEventCollector()

        with(binding) {
            convertButton.setOnClickListener { viewModel.convertButtonClick() }
            swapButton.setOnClickListener { viewModel.swapButtonClick() }
            addTransactionButton.setOnClickListener {
                viewModel.addTransactionButtonClick(
                    activityViewModel.selectedAccount.value ?: activityViewModel.accounts.value[0],
                    viewModel.resultValue.toFloat()
                )
            }
        }

        binding.convertButton.setOnClickListener { viewModel.convertButtonClick() }
        binding.swapButton.setOnClickListener { viewModel.swapButtonClick() }

    }

    private fun setupConversionCollector() {
        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collectLatest {
                when (it) {
                    is CurrencyConverterViewModel.ConversionState.Ready -> {
                        with(binding) {
                            progressBar.isVisible = false
                            resultText.text = it.result
                            addTransactionButton.isVisible = true
                        }
                    }
                    is CurrencyConverterViewModel.ConversionState.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is CurrencyConverterViewModel.ConversionState.Error -> {
                        binding.progressBar.isVisible = false
                        showToast(requireContext(), it.error)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupEventCollector() {
        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is CurrencyConverterViewModel.Event.Convert -> {
                        val amount =
                            binding.amountTextField.editText?.text.toString().toDoubleOrNull()
                        val from = binding.fromCurrency.selectedItem.toString()
                        val to = binding.toCurrency.selectedItem.toString()

                        if (amount != null) {
                            viewModel.convert(amount, from, to)
                        } else showToast(requireContext(), getString(R.string.enter_amount_error))
                    }
                    is CurrencyConverterViewModel.Event.Swap -> {
                        val fromPosition = binding.fromCurrency.selectedItemPosition
                        val toPosition = binding.toCurrency.selectedItemPosition

                        binding.fromCurrency.setSelection(toPosition)
                        binding.toCurrency.setSelection(fromPosition)
                    }
                    is CurrencyConverterViewModel.Event.OpenTheAddTransactionSheet -> {
                        if (getCurrentDestination() == this@CurrencyConverterFragment.javaClass.name) {
                            findNavController().navigate(
                                CurrencyConverterFragmentDirections.actionCurrencyConverterFragmentToSelectCategorySheetFragment(
                                    it.account, it.amount
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    private fun getCurrentDestination() =
        (findNavController().currentDestination as? FragmentNavigator.Destination)?.className
            ?: (findNavController().currentDestination as? DialogFragmentNavigator.Destination)?.className
}
