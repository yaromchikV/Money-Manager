package com.yaromchikv.moneymanager.feature.presentation.ui.transactions.add.enter_amount

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.databinding.SheetFragmentEnterTheAmountBinding
import com.yaromchikv.moneymanager.feature.domain.model.Transaction
import com.yaromchikv.moneymanager.feature.presentation.utils.mapOfColors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class EnterTheAmountSheetFragment : BottomSheetDialogFragment() {

    private var _binding: SheetFragmentEnterTheAmountBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EnterTheAmountViewModel by viewModels()

    private val args by navArgs<EnterTheAmountSheetFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SheetFragmentEnterTheAmountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val account = args.selectedAccount
        val category = args.selectedCategory

        binding.accountName.text = account.name
        binding.categoryName.text = category.name

        binding.accountBackground.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                mapOfColors[account.color] ?: R.color.orange_red
            )
        )
        binding.categoryBackground.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                mapOfColors[category.iconColor] ?: R.color.orange_red
            )
        )

        binding.applyButton.setOnClickListener {
            viewModel.applyButtonClick()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is EnterTheAmountViewModel.Event.AddTransaction -> {
                        if (account.id != null && category.id != null) {
                            val transaction = Transaction(
                                note = binding.noteTextField.editText?.text.toString(),
                                amount = binding.expenseTextField.editText?.text.toString()
                                    .toDoubleOrNull() ?: 0.0,
                                accountId = account.id,
                                categoryId = category.id
                            )

                            viewModel.addTransaction(transaction)
                            if (getCurrentDestination() == this@EnterTheAmountSheetFragment.javaClass.name) {
                                findNavController().navigate(
                                    EnterTheAmountSheetFragmentDirections
                                        .actionEnterTheAmountSheetFragmentToTransactionsFragment()
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun getCurrentDestination() =
        (findNavController().currentDestination as? FragmentNavigator.Destination)?.className
            ?: (findNavController().currentDestination as? DialogFragmentNavigator.Destination)?.className
}