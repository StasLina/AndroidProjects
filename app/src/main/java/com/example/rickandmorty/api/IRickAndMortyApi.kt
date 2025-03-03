package com.example.rickandmorty.api

import retrofit2.Response
import retrofit2.http.GET

interface IRickAndMortyApi {
    @GET("character")
    suspend fun getCharacters(): Response<CharacterResponse>
}

