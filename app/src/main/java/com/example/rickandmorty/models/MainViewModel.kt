package com.example.rickandmorty.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.api.CharacterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch


import retrofit2.Response

@HiltViewModel
class MainViewModel @Inject constructor(
    private val characterRepository: CharacterRepository
) : ViewModel() {
    private var _selectionData: MutableLiveData<CharacterResponse> = MutableLiveData()
    val getSelectionData: LiveData<CharacterResponse> get() = _selectionData

    private val _errorMessage = MutableLiveData<String>()
    val getErrorMessage: LiveData<String> get() = _errorMessage

    fun loadRickAndMortyItems() {
        viewModelScope.launch {
            try {
                val response: Response<CharacterResponse> = characterRepository.getCharacter()
                if (response.isSuccessful) {
                    _selectionData.postValue(response.body())
                } else {
                    _errorMessage.postValue("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Exception: ${e.message}")
            }
        }
    }
}

