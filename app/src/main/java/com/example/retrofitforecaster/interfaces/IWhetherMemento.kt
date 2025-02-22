package com.example.retrofitforecaster.interfaces

import com.example.retrofitforecaster.whether.WeatherForecastResponse

interface IWhetherMemento {
    interface IWhetherMemento{
        fun isEquals(otherInstance: WeatherForecastResponse) : Boolean
        fun save(otherInstance: WeatherForecastResponse)
        fun get() : WeatherForecastResponse
    }
}