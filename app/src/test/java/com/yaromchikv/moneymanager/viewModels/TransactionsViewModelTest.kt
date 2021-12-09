package com.yaromchikv.moneymanager.viewModels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.yaromchikv.moneymanager.TestCoroutineRule
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.model.TransactionView
import com.yaromchikv.moneymanager.feature.domain.usecases.DeleteTransactionByIdUseCase
import com.yaromchikv.moneymanager.feature.domain.usecases.GetTransactionViewsUseCase
import com.yaromchikv.moneymanager.feature.domain.usecases.GetTransactionsWithDayInfoUseCase
import com.yaromchikv.moneymanager.feature.presentation.ui.transactions.TransactionsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import java.time.LocalDate
import java.time.LocalTime

@ExperimentalCoroutinesApi
class TransactionsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var getTransactionViewsUseCase: GetTransactionViewsUseCase

    @Mock
    private lateinit var getTransactionsWithDayInfoUseCase: GetTransactionsWithDayInfoUseCase

    @Mock
    private lateinit var deleteTransactionByIdUseCase: DeleteTransactionByIdUseCase

    private val mockTransaction = TransactionView(
        3, "third", 678.7, LocalDate.parse("2020-12-08"),
        LocalTime.parse("14:14:20"), 3, "category3", 4,
        "account3", 5, "#FFFF00"
    )
    private val mockAccount = Account(1, "first", 100.0, "#FFFFFF")

    private lateinit var viewModel: TransactionsViewModel

    @Before
    fun setup() {
        getTransactionViewsUseCase = Mockito.mock(GetTransactionViewsUseCase::class.java)
        deleteTransactionByIdUseCase = Mockito.mock(DeleteTransactionByIdUseCase::class.java)
        getTransactionsWithDayInfoUseCase =
            Mockito.mock(GetTransactionsWithDayInfoUseCase::class.java)

        viewModel = TransactionsViewModel(
            getTransactionViewsUseCase,
            getTransactionsWithDayInfoUseCase,
            deleteTransactionByIdUseCase
        )
    }

    @Test
    fun eventsTest() = coroutineRule.testDispatcher.runBlockingTest {
        viewModel.events.test {
            viewModel.selectDateClick()
            assertEquals(TransactionsViewModel.Event.SelectDate, awaitItem())

            viewModel.addTransactionClick(mockAccount)
            assertEquals(
                TransactionsViewModel.Event.OpenTheAddTransactionSheet(mockAccount),
                awaitItem()
            )

            viewModel.deleteButtonClick(mockTransaction)
            assertEquals(
                TransactionsViewModel.Event.ShowTheDeleteTransactionDialog(mockTransaction),
                awaitItem()
            )

            viewModel.deleteConfirmationButtonClick(mockTransaction)
            assertEquals(
                TransactionsViewModel.Event.DeleteTransaction(mockTransaction),
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }
}
