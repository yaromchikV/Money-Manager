package com.yaromchikv.moneymanager.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.yaromchikv.moneymanager.TestCoroutineRule
import com.yaromchikv.moneymanager.feature.domain.model.CategoryView
import com.yaromchikv.moneymanager.feature.domain.usecases.GetCategoryViewsUseCase
import com.yaromchikv.moneymanager.feature.presentation.ui.chart.ChartViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class ChartViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var getCategoryViewsUseCase: GetCategoryViewsUseCase

    private val mockCategories = listOf(
        CategoryView(1, "first", 2, "#FFFFFF", 3.42),
        CategoryView(2, "second", 3, "#000000", 6.76),
        CategoryView(3, "third", 4, "#F0F0F0", 11.94),
    )

    private lateinit var viewModel: ChartViewModel

    @Before
    fun setup() {
        getCategoryViewsUseCase = Mockito.mock(GetCategoryViewsUseCase::class.java)
        val mockGetCategoryViewsFlow = flow {
            emit(mockCategories)
        }
        Mockito.`when`(getCategoryViewsUseCase.invoke(null to null, null))
            .thenReturn(mockGetCategoryViewsFlow)

        viewModel = ChartViewModel(getCategoryViewsUseCase)
    }

    @Test
    fun getCategoryViewsTest() = coroutineRule.testDispatcher.runBlockingTest {
        assertEquals(mockCategories, viewModel.categoryViews.value)
    }

    @Test
    fun eventsTest() = coroutineRule.testDispatcher.runBlockingTest {
        viewModel.events.test {
            viewModel.selectDateClick()
            assertEquals(ChartViewModel.Event.SelectDate, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
