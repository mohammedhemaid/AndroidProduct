package com.app.androidproductstest.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.androidproductstest.utils.Event

abstract class BaseViewModel : ViewModel() {

    private val _progressBar = MutableLiveData<Boolean>()
    open val progressBar: LiveData<Boolean> = _progressBar

    private val _showToast = MutableLiveData<Event<Int>>()
    open val showToast: LiveData<Event<Int>> = _showToast

    private val _showToastString = MutableLiveData<Event<String>>()
    open val showToastString: LiveData<Event<String>> = _showToastString

    open fun showProgressbar(show: Boolean) {
        _progressBar.value = show
    }

    open fun showToastMessage(message: Int) {
        _showToast.value = Event(message)
    }

    open fun showToastMessage(message: String?) {
        _showToastString.value = Event(message ?: "Something went worng")
    }
}