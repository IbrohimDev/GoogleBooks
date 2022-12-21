package com.example.googlebooks.di.database

import android.content.Context
import androidx.room.Room
import com.example.googlebooks.data.local.appDatabase.AppDatabase
import com.example.googlebooks.data.local.dao.StarDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "Books").build()


    @Provides
    fun provideChannelDao(appDatabase: AppDatabase): StarDao = appDatabase.starDao()


}