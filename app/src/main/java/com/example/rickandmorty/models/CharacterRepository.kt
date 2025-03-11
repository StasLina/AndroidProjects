package com.example.rickandmorty.models

import com.example.rickandmorty.api.CharacterResponse
import com.example.rickandmorty.api.IRickAndMortyApi
import jakarta.inject.Inject
import retrofit2.Response

class CharacterRepository @Inject constructor(
    private val apiService: IRickAndMortyApi
) {
    suspend fun getCharacter(): Response<CharacterResponse> {
        return apiService.getCharacters()
    }
}