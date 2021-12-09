package com.yaromchikv.moneymanager.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.yaromchikv.moneymanager.TestCoroutineRule
import com.yaromchikv.moneymanager.feature.domain.usecases.ConvertCurrencyUseCase
import com.yaromchikv.moneymanager.feature.presentation.ui.converter.CurrencyConverterViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class CurrencyConverterViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var convertCurrencyUseCase: ConvertCurrencyUseCase

    private lateinit var viewModel: CurrencyConverterViewModel

    @Before
    fun setup() {
        convertCurrencyUseCase = Mockito.mock(ConvertCurrencyUseCase::class.java)
        viewModel = CurrencyConverterViewModel(convertCurrencyUseCase)
    }

    @Test
    fun eventsTest() = coroutineRule.testDispatcher.runBlockingTest {
        viewModel.events.test {
            viewModel.convertButtonClick()
            assertEquals(CurrencyConverterViewModel.Event.Convert, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
