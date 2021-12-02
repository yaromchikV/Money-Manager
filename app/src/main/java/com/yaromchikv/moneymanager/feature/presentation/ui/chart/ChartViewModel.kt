package com.yaromchikv.moneymanager.feature.presentation.ui.chart

import android.content.SharedPreferences
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
    private val sharedPreferences: SharedPreferences,
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    private val _categoriesWithAmount = MutableStateFlow(emptyList<CategoryView>())
    val categoryWithAmount = _categoriesWithAmount.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private var getCategoriesWithAmountJob: Job? = null

    init {
        getCategoriesWithAmount()
    }

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

    fun getPreferences() = sharedPreferences

    sealed class Event {
        object SelectDate : Event()
    }
}