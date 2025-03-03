package com.example.rickandmorty.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rickandmorty.api.CharacterResponse

class MainViewModel : ViewModel() {
    private var _selectionData : MutableLiveData<CharacterResponse> = MutableLiveData<CharacterResponse>()
    val getSelectionData : LiveData<CharacterResponse> get() = _selectionData
    fun setSelectedData(newValue: CharacterResponse) {
        _selectionData.postValue(newValue);
    }
//    private var _weatherStore : WeatherStore = WeatherStore()
//    val getWeatherStore : WeatherStore get() = _weatherStore;
//
//    private var _selectedTown: MutableLiveData<String> = MutableLiveData<String>("Город не выбран");
//    val getSelectedTown: LiveData<String> get() = _selectedTown;
//    fun setSelectedTown(newValue: String) {
//        _selectedTown.postValue(newValue);
//    }
//
//    private var _typeOfTemperatureScale: MutableLiveData<String> = MutableLiveData<String>(
//        IWhetherApiService.METRIC);
//    val getTypeOfTemperatureScale: LiveData<String> get() = _typeOfTemperatureScale;
//    fun setTypeOfTemperatureScale(newValue: String) {
//        _typeOfTemperatureScale.postValue(newValue);
//    }
}