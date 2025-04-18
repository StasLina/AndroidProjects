package com.example.canvas.di

import com.example.canvas.data.repository.ImageRepositoryImpl
import com.example.canvas.domain.repository.ImageRepository
import com.example.canvas.domain.usecase.LoadImageUseCase
import com.example.canvas.domain.usecase.SaveImageUseCase
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
    fun provideImageRepository(): ImageRepository = ImageRepositoryImpl()

    @Provides
    @Singleton
    fun provideLoadImageUseCase(repository: ImageRepository): LoadImageUseCase {
        return LoadImageUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveImageUseCase(repository: ImageRepository): SaveImageUseCase {
        return SaveImageUseCase(repository)
    }
}