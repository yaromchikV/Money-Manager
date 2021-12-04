package com.yaromchikv.moneymanager.feature.presentation.ui.transactions.date

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.yaromchikv.moneymanager.R
import com.yaromchikv.moneymanager.common.DateUtils.DAY_IN_MS
import com.yaromchikv.moneymanager.common.DateUtils.getCurrentLocalDate
import com.yaromchikv.moneymanager.common.DateUtils.toLocalDate
import com.yaromchikv.moneymanager.common.DateUtils.toMilliseconds
import com.yaromchikv.moneymanager.databinding.DialogFragmentSelectDateBinding
import com.yaromchikv.moneymanager.feature.presentation.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SelectDateDialogFragment : DialogFragment(R.layout.dialog_fragment_select_date) {

    private val binding: DialogFragmentSelectDateBinding by viewBinding()

    private val viewModel: SelectDateViewModel by viewModels()
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectDate.setOnClickListener {
            viewModel.selectDateClick()
        }
        binding.today.setOnClickListener {
            viewModel.selectTodayClick()
        }
        binding.week.setOnClickListener {
            viewModel.selectWeekClick()
        }
        binding.month.setOnClickListener {
            viewModel.selectMonthClick()
        }
        binding.year.setOnClickListener {
            viewModel.selectYearClick()
        }
        binding.allTime.setOnClickListener {
            viewModel.selectAllTimeClick()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.events.collectLatest {
                when (it) {
                    is SelectDateViewModel.Event.SelectDate -> {
                        val datePicker = MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Select date")
                            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                            .build()

                        datePicker.addOnPositiveButtonClickListener { milliseconds ->
                            val date = milliseconds.toLocalDate()
                            activityViewModel.setCurrentDateRange(date, date)
                            dismiss()
                        }
                        datePicker.show(childFragmentManager, DATE_PICKER_TAG)
                    }
                    is SelectDateViewModel.Event.SelectToday -> {
                        val date = getCurrentLocalDate()
                        activityViewModel.setCurrentDateRange(date, date)
                        dismiss()
                    }
                    is SelectDateViewModel.Event.SelectWeek -> {
                        val from = (getCurrentLocalDate().toMilliseconds() - (7 * DAY_IN_MS))
                            .toLocalDate()
                        activityViewModel.setCurrentDateRange(from, null)
                        dismiss()
                    }
                    is SelectDateViewModel.Event.SelectMonth -> {
                        val from = (getCurrentLocalDate().toMilliseconds() - (30 * DAY_IN_MS))
                            .toLocalDate()
                        activityViewModel.setCurrentDateRange(from, null)
                        dismiss()
                    }
                    is SelectDateViewModel.Event.SelectYear -> {
                        val from = (getCurrentLocalDate().toMilliseconds() - (365 * DAY_IN_MS))
                            .toLocalDate()
                        activityViewModel.setCurrentDateRange(from, null)
                        dismiss()
                    }
                    is SelectDateViewModel.Event.SelectAllTime -> {
                        activityViewModel.setCurrentDateRange(null, null)
                        dismiss()
                    }
                }
            }
        }
    }

    companion object {
        private const val DATE_PICKER_TAG = "date_picker_tag"
    }
}