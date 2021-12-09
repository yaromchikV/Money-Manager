package com.yaromchikv.moneymanager.feature.presentation.ui.transactions.date

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaromchikv.moneymanager.common.DateUtils.DAY_IN_MS
import com.yaromchikv.moneymanager.common.DateUtils.getCurrentLocalDate
import com.yaromchikv.moneymanager.common.DateUtils.toLocalDate
import com.yaromchikv.moneymanager.common.DateUtils.toMilliseconds
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class SelectDateViewModel : ViewModel() {

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    fun selectDateClick() {
        viewModelScope.launch {
            _events.emit(Event.SelectDate)
        }
    }

    fun selectTodayClick() {
        viewModelScope.launch {
            _events.emit(Event.SelectToday)
        }
    }

    fun selectWeekClick() {
        viewModelScope.launch {
            _events.emit(Event.SelectWeek)
        }
    }

    fun selectMonthClick() {
        viewModelScope.launch {
            _events.emit(Event.SelectMonth)
        }
    }

    fun selectYearClick() {
        viewModelScope.launch {
            _events.emit(Event.SelectYear)
        }
    }

    fun selectAllTimeClick() {
        viewModelScope.launch {
            _events.emit(Event.SelectAllTime)
        }
    }

    fun getTheDate(daysAgo: Int = 0): LocalDate {
        return if (daysAgo != 0)
            (getCurrentLocalDate().toMilliseconds() - (daysAgo * DAY_IN_MS)).toLocalDate()
        else getCurrentLocalDate()
    }

    sealed class Event {
        object SelectDate : Event()
        object SelectToday : Event()
        object SelectWeek : Event()
        object SelectMonth : Event()
        object SelectYear : Event()
        object SelectAllTime : Event()
    }
}
