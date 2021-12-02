package com.yaromchikv.moneymanager.feature.presentation.ui.chart

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaromchikv.moneymanager.common.getCurrentLocalDate
import com.yaromchikv.moneymanager.feature.domain.model.CategoryView
import com.yaromchikv.moneymanager.feature.domain.usecase.CategoryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    private val _categoryViews = MutableStateFlow(emptyList<CategoryView>())
    val categoryViews = _categoryViews.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private var getCategoriesWithAmountJob: Job? = null

    init {
        getCategoryViews()
    }

    private fun getCategoryViews(from: LocalDate? = null, to: LocalDate? = null) {
        getCategoriesWithAmountJob?.cancel()
        getCategoriesWithAmountJob =
            categoryUseCases.getCategoryViews(from, to)
                .onEach { categories -> _categoryViews.value = categories }
                .launchIn(viewModelScope)
    }

    fun setDateRange(from: LocalDate?, to: LocalDate?) {
        getCategoryViews(from, to)
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