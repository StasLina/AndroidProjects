package com.example.rickandmorty.API

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface IRickAndMortyApi {
    @GET("character")
    suspend fun getCharacters(): Response<CharacterResponse>
}

