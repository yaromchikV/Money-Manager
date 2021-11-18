package com.yaromchikv.moneymanager.ui.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TransactionsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is transactions Fragment"
    }
    val text: LiveData<String> = _text
}