package com.yaromchikv.moneymanager.feature.presentation.ui.chart

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.DialogFragmentNavigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.common.DateUtils.toAmountFormat
import com.yaromchikv.moneymanager.databinding.FragmentChartBinding
import com.yaromchikv.moneymanager.databinding.ItemCategoryBinding
import com.yaromchikv.moneymanager.feature.domain.model.CategoryView
import com.yaromchikv.moneymanager.feature.presentation.MainActivityViewModel
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.MAIN_COLOR
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.setIcon
import com.yaromchikv.moneymanager.feature.presentation.utils.Utils.setTint
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class ChartFragment : Fragment(R.layout.fragment_chart) {

    private val binding: FragmentChartBinding by viewBinding()

    private val viewModel: ChartViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        lifecycleScope.launchWhenStarted {
            viewModel.categoryViews.collectLatest {
                updateChartData(it)
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
                    is ChartViewModel.Event.SelectDate -> {
                        if (getCurrentDestination() == this@ChartFragment.javaClass.name) {
                            findNavController().navigate(
                                ChartFragmentDirections.actionChartFragmentToSelectDateDialogFragment()
                            )
                        }
                    }
                }
            }
        }
    }

    private var currentCurrency: String = ""

    override fun onStart() {
        super.onStart()
        if (currentCurrency != activityViewModel.getCurrency())
            updateChartData(viewModel.categoryViews.value)
    }

    override fun onStop() {
        super.onStop()
        currentCurrency = activityViewModel.getCurrency()
    }

    private fun getThemeColor(color: Int): Int {
        val value = TypedValue()
        requireContext().theme.resolveAttribute(color, value, true)
        return value.data
    }

    private fun updateChartData(categoryViews: List<CategoryView>) {
        if (categoryViews.isNotEmpty()) {
            val currency = activityViewModel.getCurrency()

            updateCategories(categoryViews, currency)

            var amount = 0.0
            val entries = ArrayList<PieEntry>()
            val colors = ArrayList<Int>()

            categoryViews.forEach { category ->
                if (category.amount != 0.0) {
                    entries.add(PieEntry(category.amount.toFloat()))
                    colors.add(Color.parseColor(category.iconColor))
                    amount += category.amount
                }
            }

            if (amount == 0.0) {
                entries.add(PieEntry(1f))
                colors.add(Color.parseColor(MAIN_COLOR))
                binding.chart.alpha = 0.3f
            } else binding.chart.alpha = 1f

            val dataSet = PieDataSet(entries, "")
            dataSet.colors = colors
            dataSet.setDrawValues(false)
            dataSet.sliceSpace = 2f

            val amountString = amount.toAmountFormat(withMinus = false) + ' ' + currency
            binding.chart.apply {
                isDrawHoleEnabled = true
                holeRadius = 86f
                setHoleColor(Color.TRANSPARENT)
                setCenterTextColor(getThemeColor(R.attr.colorOnSecondary))
                centerText = "Expenses\n$amountString"
                setCenterTextSize(20f)
                description.isEnabled = false
                legend.isEnabled = false

                data = PieData(dataSet)
            }

            binding.chart.invalidate()
            binding.chart.animateY(1000, Easing.EaseInOutQuad)
        }
    }

    private fun updateCategories(categoryViews: List<CategoryView>, currency: String?) {
        binding.category1.setCategoryAttributes(categoryViews[0], currency)
        binding.category2.setCategoryAttributes(categoryViews[1], currency)
        binding.category3.setCategoryAttributes(categoryViews[2], currency)
        binding.category4.setCategoryAttributes(categoryViews[3], currency)
        binding.category5.setCategoryAttributes(categoryViews[4], currency)
        binding.category6.setCategoryAttributes(categoryViews[5], currency)
        binding.category7.setCategoryAttributes(categoryViews[6], currency)
        binding.category8.setCategoryAttributes(categoryViews[7], currency)
        binding.category9.setCategoryAttributes(categoryViews[8], currency)
        binding.category10.setCategoryAttributes(categoryViews[9], currency)
        binding.category11.setCategoryAttributes(categoryViews[10], currency)
        binding.category12.setCategoryAttributes(categoryViews[11], currency)
    }

    private fun ItemCategoryBinding.setCategoryAttributes(
        categoryView: CategoryView,
        currency: String?
    ) {
        this.name.text = categoryView.name
        this.icon.setIcon(categoryView.icon)
        this.iconBackground.setTint(categoryView.iconColor)
        this.amount.text = categoryView.amount.toAmountFormat(withMinus = false)
        this.currency.text = currency

        val color = Color.parseColor(categoryView.iconColor)

        this.amount.setTextColor(color)
        this.currency.setTextColor(color)

        this.item.alpha = if (categoryView.amount == 0.0) 0.3f else 1f
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.date_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.select_date) {
            viewModel.selectDateClick()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getCurrentDestination() =
        (findNavController().currentDestination as? FragmentNavigator.Destination)?.className
            ?: (findNavController().currentDestination as? DialogFragmentNavigator.Destination)?.className

}
