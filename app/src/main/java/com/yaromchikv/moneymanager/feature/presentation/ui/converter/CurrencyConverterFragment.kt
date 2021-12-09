package com.yaromchikv.moneymanager.feature.presentation.ui.converter

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.databinding.FragmentCurrencyConverterBinding
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CurrencyConverterFragment : Fragment(R.layout.fragment_currency_converter) {

    private val binding: FragmentCurrencyConverterBinding by viewBinding()

    private val viewModel: CurrencyConverterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.convertButton.setOnClickListener {
            viewModel.convertButtonClick()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collectLatest {
                when (it) {
                    is CurrencyConverterViewModel.ConversionState.Ready -> {
                        binding.progressBar.isVisible = false
                        binding.resultText.text = it.result
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

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                val amount = binding.amountTextField.editText?.text.toString().toDoubleOrNull()
                val from = binding.fromCurrency.selectedItem.toString()
                val to = binding.toCurrency.selectedItem.toString()

                if (amount != null) {
                    viewModel.convert(amount, from, to)
                } else showToast(requireContext(), getString(R.string.enter_amount_error))
            }
        }
    }
}