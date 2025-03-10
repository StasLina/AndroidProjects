package com.example.rickandmorty.di

import com.example.rickandmorty.api.IRickAndMortyApi
import com.example.rickandmorty.api.RetrofitHelper
import com.example.rickandmorty.models.CharacterRepository
import com.example.rickandmorty.models.MainViewModelFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApiService(): IRickAndMortyApi {
        // Создание и возврат экземпляра ApiService
        return RetrofitHelper.getInstance().create(IRickAndMortyApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCharacterRepository(apiService: IRickAndMortyApi): CharacterRepository {
        return CharacterRepository(apiService)
    }

    @Provides
    @Singleton
    fun provideMainViewModelFactory(characterRepository: CharacterRepository): MainViewModelFactory {
        return MainViewModelFactory(characterRepository)
    }
}