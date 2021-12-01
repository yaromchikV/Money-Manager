package com.yaromchikv.moneymanager.feature.presentation.ui.transactions.date

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

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

    sealed class Event {
        object SelectDate : Event()
        object SelectToday : Event()
        object SelectWeek : Event()
        object SelectMonth : Event()
        object SelectYear : Event()
        object SelectAllTime : Event()
    }
}