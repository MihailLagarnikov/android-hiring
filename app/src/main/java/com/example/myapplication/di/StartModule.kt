package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.data.RepositoryImpl
import com.example.myapplication.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StartModule {

    @Singleton
    @Provides
    fun provideRepository(@ApplicationContext context: Context): Repository {
        return RepositoryImpl(context)
    }
}