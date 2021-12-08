package com.yaromchikv.moneymanager.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.yaromchikv.moneymanager.TestCoroutineRule
import com.yaromchikv.moneymanager.feature.domain.usecases.AddTransactionUseCase
import com.yaromchikv.moneymanager.feature.presentation.ui.transactions.add.AddTransactionViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class AddTransactionViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    private lateinit var viewModel: AddTransactionViewModel

    @Before
    fun setup() {
        val addTransactionUseCase = Mockito.mock(AddTransactionUseCase::class.java)
        viewModel = AddTransactionViewModel(addTransactionUseCase)
    }

    @Test
    fun eventsTest() = coroutineRule.testDispatcher.runBlockingTest {
        viewModel.events.test {
            viewModel.applyButtonClick()
            assertEquals(AddTransactionViewModel.Event.AddTransaction, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

}