package com.example.prenotes.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.prenotes.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainActivityModel @Inject constructor(noteRepository: NoteRepository) :
    NoteViewModel(noteRepository) {
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun setErrorMessage(message: String) {
        _errorMessage.value = message
    }

}