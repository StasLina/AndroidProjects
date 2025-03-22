package com.example.rickandmorty.models

import com.example.rickandmorty.api.Character
import com.example.rickandmorty.api.CharacterResponse
import com.example.rickandmorty.api.Info
import com.example.rickandmorty.api.Location
import com.example.rickandmorty.api.Origin

// Тестовые данные для Info
val testInfo = Info(
    count = 826,
    pages = 42,
    next = "https://rickandmortyapi.com/api/character?page=2",
    prev = null
)

// Тестовые данные для Origin
val testOrigin = Origin(
    name = "Earth",
    url = "https://rickandmortyapi.com/api/location/1"
)

// Тестовые данные для Location
val testLocation = Location(
    name = "Earth",
    url = "https://rickandmortyapi.com/api/location/1"
)

// Тестовые данные для Character
val testCharacter = Character(
    id = 1,
    name = "Rick Sanchez",
    status = "Alive",
    species = "Human",
    type = "",
    gender = "Male",
    origin = testOrigin,
    location = testLocation,
    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
    episode = listOf("https://rickandmortyapi.com/api/episode/1"),
    url = "https://rickandmortyapi.com/api/character/1",
    created = "2017-11-04T18:48:46.250Z"
)

// Тестовые данные для CharacterResponse
val testCharacterResponse = CharacterResponse(
    info = testInfo,
    results = listOf(testCharacter) // Можно добавить больше персонажей в список
)