package com.example.finnub.di

import android.content.Context
import com.example.finnub.data.datastore.SaveStockRepositoryImpl
import com.example.finnub.domain.SaveStockRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class DataStoreModule {

    @Provides
    @Singleton
    fun provideSaveStockRepository(@ApplicationContext context: Context):SaveStockRepository{
        return SaveStockRepositoryImpl(
            context = context
        )
    }
}