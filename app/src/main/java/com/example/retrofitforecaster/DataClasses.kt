package com.example.retrofitforecaster

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// Data class for temperature
data class Main(
    @SerializedName("temp") val temp: Double
) {
    fun getTempAsString(): String = "${temp}° C"
}

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

// Retrofit interface to fetch the forecast data
interface DayGetter {
    @GET("forecast?q=Shklov,by&units=metric")
    suspend fun check(@Query("appid") apiKey: String): Response<DataResponse>
}
