package com.yaromchikv.moneymanager.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.yaromchikv.moneymanager.TestCoroutineRule
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.usecases.DeleteAccountUseCase
import com.yaromchikv.moneymanager.feature.presentation.ui.accounts.actions.AccountActionsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class AccountActionsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    private val mockAccounts = listOf(
        Account(1, "first", 100.0, "#FFFFFF"),
        Account(2, "second", 55.5, "#000000"),
        Account(3, "third", 11.76, "#F0F0F0")
    )

    private lateinit var viewModel: AccountActionsViewModel

    @Before
    fun setup() {
        val deleteAccountUseCase = Mockito.mock(DeleteAccountUseCase::class.java)
        viewModel = AccountActionsViewModel(deleteAccountUseCase)
    }

    @Test
    fun eventsTest() = coroutineRule.testDispatcher.runBlockingTest {
        viewModel.events.test {
            viewModel.editButtonClick(mockAccounts[0])
            assertEquals(
                AccountActionsViewModel.Event.NavigateToEditAccountScreen(mockAccounts[0]),
                awaitItem()
            )
            viewModel.deleteButtonClick(mockAccounts[1])
            assertEquals(
                AccountActionsViewModel.Event.ShowTheDeleteAccountDialog(mockAccounts[1]),
                awaitItem()
            )
            viewModel.deleteConfirmationButtonClick()
            assertEquals(AccountActionsViewModel.Event.DeleteAccount, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}
