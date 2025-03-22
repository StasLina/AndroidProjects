package com.example.rickandmorty.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RickAndMortyApi {
    object RetrofitHelper {
        private const val BASE_URL = "https://rickandmortyapi.com/api/"

        fun getInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}