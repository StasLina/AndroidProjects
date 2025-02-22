package com.example.retrofitforecaster.whether

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IWhetherApiService {
    companion object UNITS {
        const val METRIC = "metric" // Цельсий
        const val IMPERIAL = "imperial" // Фаренгейт
    }

    @GET("forecast")
    suspend fun getWeatherForecastByCityName(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = UNITS.METRIC,
        @Query("lang") lang: String = "ru"
    ): Response<WeatherForecastResponse>
}