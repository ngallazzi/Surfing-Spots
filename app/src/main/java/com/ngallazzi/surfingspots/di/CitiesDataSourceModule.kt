package com.ngallazzi.surfingspots.di

import com.ngallazzi.surfingspots.data.cities.CitiesDataSource
import com.ngallazzi.surfingspots.data.cities.local.CitiesLocalDataSource
import com.ngallazzi.surfingspots.data.cities.remote.CitiesApi
import com.ngallazzi.surfingspots.data.cities.remote.CitiesRemoteDataSource
import com.ngallazzi.surfingspots.data.database.CityDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Qualifier

@Module
@InstallIn(ApplicationComponent::class)
object CitiesDataSourceModule {
    @LocalDataSource
    @Provides
    fun provideCitiesLocalDataSource(
        cityDao: CityDao,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): CitiesDataSource {
        return CitiesLocalDataSource(cityDao, dispatcher)
    }

    @RemoteDataSource
    @Provides
    fun provideCitiesRemoteDataSource(
        service: CitiesApi
    ): CitiesDataSource {
        return CitiesRemoteDataSource(service)
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalDataSource

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RemoteDataSource