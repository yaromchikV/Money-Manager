package com.yaromchikv.moneymanager.feature.presentation.ui.transactions.add

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.common.DateUtils.toAmountFormat
import com.yaromchikv.moneymanager.databinding.SheetFragmentAddTransactionBinding
import com.yaromchikv.moneymanager.feature.domain.model.Transaction
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.setIcon
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AddTransactionSheetFragment : BottomSheetDialogFragment() {

    private var _binding: SheetFragmentAddTransactionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddTransactionViewModel by viewModels()

    private val args by navArgs<AddTransactionSheetFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SheetFragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupContainerAppearance()
        setupEventCollector()
    }

    private fun setupContainerAppearance() {
        with(binding) {
            accountName.text = args.selectedAccount.name
            categoryName.text = args.selectedCategory.name
            categoryIcon.setIcon(args.selectedCategory.icon)

            if (args.amount != 0f)
                expenseTextField.editText?.setText(
                    args.amount.toDouble().toAmountFormat(withMinus = false)
                )

            accountBackground.setBackgroundColor(Color.parseColor(args.selectedAccount.color))
            categoryBackground.setBackgroundColor(Color.parseColor(args.selectedCategory.iconColor))
        }
    }

    private fun setupEventCollector() {
        binding.applyButton.setOnClickListener { viewModel.applyButtonClick() }

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is AddTransactionViewModel.Event.AddTransaction -> {
                        val account = args.selectedAccount
                        val category = args.selectedCategory
                        if (account.id != null) {
                            val amount =
                                binding.expenseTextField.editText?.text.toString().toDoubleOrNull()
                            if (amount == null || amount <= 0)
                                showToast(context, getString(R.string.enter_expense_error))
                            else {
                                val note = binding.noteTextField.editText?.text.toString()

                                val transaction = Transaction(
                                    note = note,
                                    amount = amount,
                                    accountId = account.id,
                                    categoryId = category.id
                                )

                                viewModel.addTransaction(transaction)

                                if (getCurrentDestination() == this@AddTransactionSheetFragment.javaClass.name) {
                                    findNavController().navigate(
                                        AddTransactionSheetFragmentDirections
                                            .actionAddTransactionSheetFragmentToTransactionsFragment()
                                    )
                                }
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
