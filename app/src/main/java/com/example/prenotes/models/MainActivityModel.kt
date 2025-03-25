package com.example.prenotes.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityModel @Inject constructor() : ViewModel() {
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Вы можете добавить методы для обновления _errorMessage
    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }
}