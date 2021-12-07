package com.yaromchikv.moneymanager.feature.presentation.ui.transactions.select_category

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.common.DateUtils.toAmountFormat
import com.yaromchikv.moneymanager.databinding.SheetFragmentSelectCategoryBinding
import com.yaromchikv.moneymanager.feature.presentation.MainActivityViewModel
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.CURRENCY_PREFERENCE_KEY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class SelectCategorySheetFragment : BottomSheetDialogFragment() {

    private var _binding: SheetFragmentSelectCategoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SelectCategoryViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    @Inject
    lateinit var categoriesRVAdapter: CategoriesRVAdapter

    private val args by navArgs<SelectCategorySheetFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = SheetFragmentSelectCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val account = args.selectedAccount

        binding.accountName.text = account.name
        binding.accountAmount.text = account.amount.toAmountFormat(withMinus = false)
        binding.accountCurrency.text = viewModel.getPreferences().getString(
            CURRENCY_PREFERENCE_KEY,
            requireContext().resources.getStringArray(R.array.currency_values)[0]
        )
        binding.actionsContainer.setBackgroundColor(Color.parseColor(account.color))

        categoriesRVAdapter.setOnClickListener(CategoriesRVAdapter.OnClickListener {
            viewModel.selectCategoryClick(account, it)
        })

        binding.gridOfCategories.apply {
            adapter = categoriesRVAdapter
        }

        lifecycleScope.launchWhenStarted {
            viewModel.categoryViews.collectLatest { newList ->
                categoriesRVAdapter.submitList(newList)
            }
        }

        lifecycleScope.launchWhenStarted {
            activityViewModel.selectedDateRange.collectLatest {
                viewModel.setDateRange(it.first, it.second)
            }
        }

        lifecycleScope.launchWhenStarted {
            activityViewModel.selectedAccount.collectLatest {
                viewModel.setSelectedAccount(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is SelectCategoryViewModel.Event.SelectCategory -> {
                        if (getCurrentDestination() == this@SelectCategorySheetFragment.javaClass.name) {
                            findNavController().navigate(
                                SelectCategorySheetFragmentDirections
                                    .actionSelectCategorySheetFragmentToAddTransactionSheetFragment(
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