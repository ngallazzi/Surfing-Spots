package com.ngallazzi.surfingspots.di

import android.content.Context
import androidx.room.Room
import com.ngallazzi.surfingspots.data.database.AppDatabase
import com.ngallazzi.surfingspots.data.database.CityDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@InstallIn(ApplicationComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "surfing_spots_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideCityDao(database: AppDatabase): CityDao {
        return database.cityDao()
    }
}

