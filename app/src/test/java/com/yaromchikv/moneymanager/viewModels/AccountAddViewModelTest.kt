package com.yaromchikv.moneymanager.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.yaromchikv.moneymanager.TestCoroutineRule
import com.yaromchikv.moneymanager.feature.domain.usecases.AddAccountUseCase
import com.yaromchikv.moneymanager.feature.presentation.ui.accounts.add.AccountAddViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class AccountAddViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    private lateinit var viewModel: AccountAddViewModel

    @Before
    fun setup() {
        val addAccountUseCase = Mockito.mock(AddAccountUseCase::class.java)
        viewModel = AccountAddViewModel(addAccountUseCase)
    }

    @Test
    fun eventsTest() = coroutineRule.testDispatcher.runBlockingTest {
        viewModel.events.test {
            viewModel.applyButtonClick()
            assertEquals(AccountAddViewModel.Event.AddAccount, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
