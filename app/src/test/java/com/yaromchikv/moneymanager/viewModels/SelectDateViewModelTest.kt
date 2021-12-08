package com.yaromchikv.moneymanager.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.yaromchikv.moneymanager.TestCoroutineRule
import com.yaromchikv.moneymanager.common.DateUtils.DAY_IN_MS
import com.yaromchikv.moneymanager.common.DateUtils.toLocalDate
import com.yaromchikv.moneymanager.feature.presentation.ui.transactions.date.SelectDateViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.*

@ExperimentalCoroutinesApi
class SelectDateViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    private lateinit var viewModel: SelectDateViewModel

    @Before
    fun setup() {
        viewModel = SelectDateViewModel()
    }

    private fun getCurrentLocalDate(): LocalDate = LocalDate.parse(
        SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    )

    private fun LocalDate.toMilliseconds(): Long =
        this.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

    @Test
    fun getTheDateTest() = coroutineRule.testDispatcher.runBlockingTest {
        assertEquals(getCurrentLocalDate(), viewModel.getTheDate())

        assertEquals(
            (getCurrentLocalDate().toMilliseconds() - (7 * DAY_IN_MS)).toLocalDate(),
            viewModel.getTheDate(7)
        )

        assertEquals(
            (getCurrentLocalDate().toMilliseconds() - (31 * DAY_IN_MS)).toLocalDate(),
            viewModel.getTheDate(31)
        )
    }

    @Test
    fun eventsTest() = coroutineRule.testDispatcher.runBlockingTest {
        viewModel.events.test {
            viewModel.selectDateClick()
            assertEquals(SelectDateViewModel.Event.SelectDate, awaitItem())

            viewModel.selectTodayClick()
            assertEquals(SelectDateViewModel.Event.SelectToday, awaitItem())

            viewModel.selectWeekClick()
            assertEquals(SelectDateViewModel.Event.SelectWeek, awaitItem())

            viewModel.selectMonthClick()
            assertEquals(SelectDateViewModel.Event.SelectMonth, awaitItem())

            viewModel.selectYearClick()
            assertEquals(SelectDateViewModel.Event.SelectYear, awaitItem())

            viewModel.selectAllTimeClick()
            assertEquals(SelectDateViewModel.Event.SelectAllTime, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

}