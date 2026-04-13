package com.yey.macflai.di

import android.app.Application
import com.yey.macflai.data.AppDatabase
import com.yey.macflai.data.MacflaiDao
import com.yey.macflai.repository.SinclairRepository
import com.yey.macflai.network.SinclairApiService
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
    fun provideAppDatabase(application: Application): AppDatabase {
        return AppDatabase.getDatabase(application)
    }

    @Provides
    @Singleton
    fun provideMacflaiDao(appDatabase: AppDatabase): MacflaiDao {
        return appDatabase.macflaiDao()
    }

    @Provides
    @Singleton
    fun provideSinclairRepository(dao: MacflaiDao, api: SinclairApiService): SinclairRepository {
        return SinclairRepository(dao, api)
    }
}
