package com.yaromchikv.moneymanager.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.yaromchikv.moneymanager.TestCoroutineRule
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.usecases.GetAccountsUseCase
import com.yaromchikv.moneymanager.feature.presentation.ui.accounts.AccountsViewModel
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
class AccountsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var getAccountsUseCase: GetAccountsUseCase

    private val mockAccounts = listOf(
        Account(1, "first", 100.0, "#FFFFFF"),
        Account(2, "second", 55.5, "#000000"),
        Account(3, "third", 11.76, "#F0F0F0")
    )

    private lateinit var viewModel: AccountsViewModel

    @Before
    fun setup() {
        getAccountsUseCase = Mockito.mock(GetAccountsUseCase::class.java)
        val mockGetAccountsFlow = flow {
            emit(mockAccounts)
        }
        Mockito.`when`(getAccountsUseCase.invoke()).thenReturn(mockGetAccountsFlow)

        viewModel = AccountsViewModel(getAccountsUseCase)
    }

    @Test
    fun getAccountsTest() = coroutineRule.testDispatcher.runBlockingTest {
        advanceTimeBy(10)
        assertEquals(mockAccounts, viewModel.accounts.value)
    }

    @Test
    fun getFullAmountTest() = coroutineRule.testDispatcher.runBlockingTest {
        val amount = mockAccounts[0].amount + mockAccounts[1].amount + mockAccounts[2].amount
        assertEquals(amount, viewModel.getFullAmount(), 0.01)
    }

    @Test
    fun eventsTest() = coroutineRule.testDispatcher.runBlockingTest {
        viewModel.events.test {
            viewModel.selectAccount(mockAccounts[0])
            assertEquals(AccountsViewModel.Event.OpenTheAccountActionsSheet(mockAccounts[0]), awaitItem())

            viewModel.addButtonClick()
            assertEquals(AccountsViewModel.Event.NavigateToAddAccountScreen, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}