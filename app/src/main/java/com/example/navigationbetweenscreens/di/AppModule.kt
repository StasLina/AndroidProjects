package com.example.navigationbetweenscreens.di


import android.content.ContentResolver
import android.content.Context
import androidx.room.Room
import com.example.navigationbetweenscreens.data.GalleryRepository
import com.example.navigationbetweenscreens.data.local.AppDatabase
import com.example.navigationbetweenscreens.data.local.ImageDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "gallery.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideImageDao(database: AppDatabase) = database.imageDao()

    @Provides
    @Singleton
    fun provideGalleryRepository(
        contentResolver: ContentResolver,
        imageDao: ImageDao
    ) = GalleryRepository(contentResolver, imageDao)

    @Provides
    fun provideContentResolver(@ApplicationContext context: Context): ContentResolver {
        return context.contentResolver
    }
}