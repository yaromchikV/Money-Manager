package com.yaromchikv.moneymanager.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.yaromchikv.moneymanager.TestCoroutineRule
import com.yaromchikv.moneymanager.feature.domain.usecases.UpdateAccountUseCase
import com.yaromchikv.moneymanager.feature.presentation.ui.accounts.edit.AccountEditViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class AccountEditViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    private lateinit var viewModel: AccountEditViewModel

    @Before
    fun setup() {
        val updateAccountUseCase = Mockito.mock(UpdateAccountUseCase::class.java)
        viewModel = AccountEditViewModel(updateAccountUseCase)
    }

    @Test
    fun eventsTest() = coroutineRule.testDispatcher.runBlockingTest {
        viewModel.events.test {
            viewModel.applyButtonClick()
            assertEquals(AccountEditViewModel.Event.UpdateAccount, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}