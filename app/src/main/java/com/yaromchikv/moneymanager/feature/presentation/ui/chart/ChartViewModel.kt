package com.yaromchikv.moneymanager.feature.presentation.ui.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaromchikv.moneymanager.feature.domain.model.CategoryView
import com.yaromchikv.moneymanager.feature.domain.usecase.CategoryUseCases
import com.yaromchikv.moneymanager.feature.domain.usecase.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases,
    private val transactionUseCases: TransactionUseCases
) : ViewModel() {

//    private val _categories = MutableStateFlow(emptyList<Category>())
//    val categories: StateFlow<List<Category>> = _categories
//
//    private val _transactions = MutableStateFlow(emptyList<Transaction>())
//    val transactions: StateFlow<List<Transaction>> = _transactions
//
//    val combineFlow = _categories.combine(_transactions) { categories, transactions ->
//        categories to transactions
//    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
//
//    private var getCategoriesJob: Job? = null
//    private var getTransactionsJob: Job? = null

    private val _categoriesWithAmount = MutableStateFlow(emptyList<CategoryView>())
    val categoryWithAmount = _categoriesWithAmount.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private var getCategoriesWithAmountJob: Job? = null

    init {
//        getCategories()
//        getTransactions()
        getCategoriesWithAmount()
    }

//    private fun getCategories() {
//        getCategoriesJob?.cancel()
//        getCategoriesJob = categoryUseCases.getCategories()
//            .onEach { categories -> _categories.value = categories }
//            .launchIn(viewModelScope)
//    }
//
//    private fun getTransactions() {
//        getTransactionsJob?.cancel()
//        getTransactionsJob = transactionUseCases.getTransactions()
//            .onEach { transactions -> _transactions.value = transactions }
//            .launchIn(viewModelScope)
//    }

    private fun getCategoriesWithAmount() {
        getCategoriesWithAmountJob?.cancel()
        getCategoriesWithAmountJob = categoryUseCases.getCategoryViews()
            .onEach { categories -> _categoriesWithAmount.value = categories }
            .launchIn(viewModelScope)
    }

    fun selectDateClick() {
        viewModelScope.launch {
            _events.emit(Event.SelectDate)
        }
    }

    sealed class Event {
        object SelectDate : Event()
    }
}