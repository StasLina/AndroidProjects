package com.example.rickandmorty.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.api.CharacterResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlinx.coroutines.launch


import retrofit2.Response

@HiltViewModel
class MainViewModel @Inject constructor(
    private val characterRepository: ICharacterRepository
) : ViewModel() {
    private var job: Job? = null

    companion object {
        const val HTTP400 = "Ошибка 400: Неверный запрос"
        const val HTTP401 = "Ошибка 401: Неавторизован"
        const val HTTP403 = "Ошибка 403: Доступ запрещен"
        const val HTTP404 = "Ошибка 403: Доступ запрещен"
        const val HTTP500 = "Ошибка 403: Доступ запрещен"
        const val HTTP503 = "Ошибка 403: Доступ запрещен"
        fun HTTPUnknown(code :Int): String  = "Ошибка ${code}: Неизвестная ошибка"
    }
    private var _selectionData: MutableLiveData<CharacterResponse> = MutableLiveData()
    val getSelectionData: LiveData<CharacterResponse> get() = _selectionData

    private val _errorMessage = MutableLiveData<String>()
    val getErrorMessage: LiveData<String> get() = _errorMessage

    fun loadRickAndMortyItems() {
        job?.cancel()

        job = viewModelScope.launch {
            try {
                val response: Response<CharacterResponse> = characterRepository.getCharacter()
                if (response.isSuccessful) {
                    _selectionData.postValue(response.body())
                } else {
                    _errorMessage.postValue(getErrorTextByHTTPCode(response.code()))
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Исключение: ${e.message}")
            }
        }
    }

    fun getErrorTextByHTTPCode(code: Int):String {
        when (code) {
            400 -> return HTTP400
            401 -> return  HTTP401
            403 -> return  HTTP403
            404 -> return  HTTP404
            500 -> return  HTTP500
            503 -> return  HTTP503
            else -> return  HTTPUnknown(code)
        }
    }

    public override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}


