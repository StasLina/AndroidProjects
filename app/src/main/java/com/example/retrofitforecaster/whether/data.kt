package com.example.retrofitforecaster.whether

import com.google.gson.annotations.SerializedName

// Data class for temperature
data class Main(
    @SerializedName("temp") val temp: Double
)

// Data class for weather
data class Weather(
    @SerializedName("main") val main: String,
    @SerializedName("icon") val icon: String
)

// Data class for day forecast
data class DayPrognosis(
    @SerializedName("dt_txt") val dt_txt: String,
    @SerializedName("main") val main: Main,
    @SerializedName("weather") val weather: List<Weather>
)

// Data response containing the list of day forecasts
data class DataResponse(
    @SerializedName("list") val list: ArrayList<DayPrognosis>
)

data class Coord (
    val lon: Double,
    val lat: Double
)
data class City(
    val id: Int,
    val name: String,
    val coord: Coord,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Long,
    val sunset: Long
)

data class WeatherForecastResponse(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: ArrayList<DayPrognosis>,
    val city: City
)