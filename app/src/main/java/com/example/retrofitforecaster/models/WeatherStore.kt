package com.example.retrofitforecaster.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.retrofitforecaster.interfaces.IWhetherMemento
import com.example.retrofitforecaster.whether.WeatherForecastResponse
import com.google.gson.Gson

class WeatherStore() : IWhetherMemento.IWhetherMemento {
    private var wethers: MutableLiveData<WeatherForecastResponse?> = MutableLiveData()

    // Свойство позволяет при изменении данных обновить список
    val getWeather: LiveData<WeatherForecastResponse?> get() = wethers
    override  fun get() : WeatherForecastResponse {
        return wethers.value!!;
    }
    override fun isEquals(otherInstance: WeatherForecastResponse) : Boolean{
        if(wethers.value == null) return false;
        return wethers.value == otherInstance;
    }

    // тип анонимных объектов - Any, поэтому `override` необходим в `toString()`
    override fun toString() : String {
        if(wethers.value == null) return "Данные не установлены"
        val gson = Gson()
        return gson.toJson(wethers.value)
    }

    override fun save(otherInstance: WeatherForecastResponse) {
        // save разрешен только из основного потока. исп. post
        wethers.postValue(otherInstance);
    }
};
