package com.yaromchikv.moneymanager.feature.presentation.ui.chart

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
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
import com.yaromchikv.moneymanager.common.toAmountFormat
import com.yaromchikv.moneymanager.databinding.FragmentChartBinding
import com.yaromchikv.moneymanager.databinding.ItemCategoryBinding
import com.yaromchikv.moneymanager.feature.domain.model.CategoryView
import com.yaromchikv.moneymanager.feature.presentation.utils.mapOfColors
import com.yaromchikv.moneymanager.feature.presentation.utils.mapOfDrawables
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ChartFragment : Fragment(R.layout.fragment_chart) {

    private val binding: FragmentChartBinding by viewBinding()

    private val viewModel: ChartViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

//        lifecycleScope.launchWhenStarted {
//            viewModel.combineFlow.collectLatest {
//                if (it != null && it.first.isNotEmpty() && it.second.isNotEmpty()) {
//                    updateChartData(it.first, it.second)
//                }
//            }
//        }

        lifecycleScope.launchWhenStarted {
            viewModel.categoryWithAmount.collectLatest {
                if (it.isNotEmpty())
                    updateChartData(it)
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

    private fun updateChartData(categoryViews: List<CategoryView>) {
        val currency = viewModel.getPreferences().getString(
            "currency",
            requireContext().resources.getStringArray(R.array.currency_values)[0]
        )

        updateCategories(categoryViews, currency)

        var amount = 0.0
        val entries = ArrayList<PieEntry>()
        val colors = ArrayList<Int>()

        categoryViews.forEach { category ->
            colors.add(
                ContextCompat.getColor(
                    requireContext(),
                    mapOfColors[category.iconColor] ?: R.color.orange_red
                )
            )

            entries.add(PieEntry(category.amount.toFloat()))
            amount += category.amount
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        dataSet.setDrawValues(false)

        val amountString = amount.toAmountFormat(withMinus = false) + ' ' + currency
        binding.chart.apply {
            isDrawHoleEnabled = true
            holeRadius = 78f
            centerText = "Expenses\n$amountString"
            setCenterTextSize(20f)
            description.isEnabled = false
            legend.isEnabled = false
            data = PieData(dataSet)
        }

        binding.chart.invalidate()
        binding.chart.animateY(1000, Easing.EaseInOutQuad)
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
        this.icon.setImageResource(mapOfDrawables[categoryView.icon] ?: 0)
        val amount = categoryView.amount.toAmountFormat(withMinus = false) + ' ' + currency
        this.amount.text = amount
        DrawableCompat.setTint(
            this.iconBackground.drawable,
            ContextCompat.getColor(
                requireContext(),
                mapOfColors[categoryView.iconColor] ?: R.color.orange_red
            )
        )
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
