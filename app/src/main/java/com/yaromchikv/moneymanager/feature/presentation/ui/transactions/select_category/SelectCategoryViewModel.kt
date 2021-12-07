package com.yaromchikv.moneymanager.feature.presentation.ui.transactions.select_category

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaromchikv.moneymanager.feature.domain.model.Account
import com.yaromchikv.moneymanager.feature.domain.model.CategoryView
import com.yaromchikv.moneymanager.feature.domain.usecase.CategoryUseCases
import com.yaromchikv.moneymanager.feature.presentation.ui.chart.ChartViewModel
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
class SelectCategoryViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val categoryUseCases: CategoryUseCases
) : ViewModel() {

    private var getCategoryViewsJob: Job? = null

    private val _categoryViews = MutableStateFlow(emptyList<CategoryView>())
    val categoryViews = _categoryViews.asStateFlow()

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private val _selectedAccount = MutableStateFlow<Account?>(null)
    private val _selectedDateRange = MutableStateFlow<Pair<LocalDate?, LocalDate?>>(null to null)

    init {
        getCategoryViews()
    }

    init {
        getCategoryViews()
    }

    private fun getCategoryViews() {
        getCategoryViewsJob?.cancel()
        getCategoryViewsJob =
            categoryUseCases.getCategoryViews(_selectedDateRange.value, _selectedAccount.value)
                .onEach { categories -> _categoryViews.value = categories }
                .launchIn(viewModelScope)
    }

    fun setDateRange(from: LocalDate? = null, to: LocalDate? = null) {
        _selectedDateRange.value = from to to
        getCategoryViews()
    }

    fun setSelectedAccount(account: Account? = null) {
        _selectedAccount.value = account
        getCategoryViews()
    }

    fun selectCategoryClick(account: Account, categoryView: CategoryView) {
        viewModelScope.launch {
            _events.emit(Event.SelectCategory(account, categoryView))
        }
    }

    sealed class Event {
        data class SelectCategory(val account: Account, val category: CategoryView) : Event()
    }
}