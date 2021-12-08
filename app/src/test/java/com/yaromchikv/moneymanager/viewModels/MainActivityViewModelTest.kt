package com.yaromchikv.moneymanager.viewModels

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.yaromchikv.moneymanager.TestCoroutineRule
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.usecases.GetAccountsUseCase
import com.yaromchikv.moneymanager.feature.presentation.MainActivityViewModel
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
class MainActivityViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var getAccountsUseCase: GetAccountsUseCase

    @Mock
    private lateinit var sharedPreferences: SharedPreferences


    private val mockAccounts = listOf(
        Account(1, "first", 100.0, "#FFFFFF"),
        Account(2, "second", 55.5, "#000000"),
        Account(3, "third", 11.76, "#F0F0F0")
    )

    private lateinit var viewModel: MainActivityViewModel

    @Before
    fun setup() {
        getAccountsUseCase = Mockito.mock(GetAccountsUseCase::class.java)
        val mockGetAccountsFlow = flow { emit(mockAccounts) }
        Mockito.`when`(getAccountsUseCase.invoke()).thenReturn(mockGetAccountsFlow)

        sharedPreferences = Mockito.mock(SharedPreferences::class.java)
        Mockito.`when`(sharedPreferences.getString("currency", null)).thenReturn("BYN")

        viewModel = MainActivityViewModel(sharedPreferences, getAccountsUseCase)
    }

    @Test
    fun getAccountsTest() = coroutineRule.testDispatcher.runBlockingTest {
        assertEquals(mockAccounts, viewModel.accounts.value)
    }

    @Test
    fun sharedPreferencesTest() = coroutineRule.testDispatcher.runBlockingTest {
        val currency = viewModel.getCurrency()
        assertEquals(currency, sharedPreferences.getString("currency", null))
    }

    @Test
    fun eventsTest() = coroutineRule.testDispatcher.runBlockingTest {
        viewModel.events.test {
            viewModel.settingsButtonClick()
            assertEquals(MainActivityViewModel.Event.OpenTheSettingsScreen, awaitItem())

            viewModel.selectAccountButtonClick()
            assertEquals(MainActivityViewModel.Event.OpenTheSelectAccountDialog, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }
}