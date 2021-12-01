package com.yaromchikv.moneymanager.feature.presentation.ui.transactions.add

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
import com.yaromchikv.moneymanager.common.toAmountFormat
import com.yaromchikv.moneymanager.databinding.SheetFragmentAddTransactionBinding
import com.yaromchikv.moneymanager.feature.presentation.utils.mapOfColors
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class AddTransactionSheetFragment : BottomSheetDialogFragment() {

    private var _binding: SheetFragmentAddTransactionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddTransactionViewModel by viewModels()

    @Inject
    lateinit var categoriesRVAdapter: CategoriesRVAdapter

    private val args by navArgs<AddTransactionSheetFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SheetFragmentAddTransactionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val account = args.selectedAccount

        binding.accountName.text = account.name
        binding.accountAmount.text = account.amount.toAmountFormat()
        binding.actionsContainer.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                mapOfColors[account.color] ?: R.color.orange_red
            )
        )

        categoriesRVAdapter.setOnClickListener(CategoriesRVAdapter.OnClickListener {
            viewModel.selectCategoryClick(account, it)
        })

        binding.gridOfCategories.apply {
            adapter = categoriesRVAdapter
        }

        lifecycleScope.launchWhenStarted {
            viewModel.categories.collectLatest { newList ->
                categoriesRVAdapter.setData(newList)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is AddTransactionViewModel.Event.SelectCategory -> {
                        if (getCurrentDestination() == this@AddTransactionSheetFragment.javaClass.name) {
                            findNavController().navigate(
                                AddTransactionSheetFragmentDirections
                                    .actionAddTransactionSheetFragmentToEnterTheAmountSheetFragment(
                                        it.account,
                                        it.category
                                    )
                            )
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