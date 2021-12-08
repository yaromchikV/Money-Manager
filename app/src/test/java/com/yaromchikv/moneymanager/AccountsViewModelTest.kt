package com.yaromchikv.moneymanager

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@ExperimentalCoroutinesApi
class AccountsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

}