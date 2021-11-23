package com.yaromchikv.moneymanager.feature.presentation.ui.chart

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ChartViewModel : ViewModel() {

    private val _text = MutableStateFlow("This is chart Fragment")
    val text: StateFlow<String> = _text
}