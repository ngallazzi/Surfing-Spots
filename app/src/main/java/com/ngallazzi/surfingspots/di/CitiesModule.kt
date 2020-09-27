package com.ngallazzi.surfingspots.di

import com.ngallazzi.surfingspots.data.cities.CitiesRepository
import com.ngallazzi.surfingspots.data.cities.CitiesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent


@Module
@InstallIn(ApplicationComponent::class)
abstract class CitiesModule {
    @Binds
    abstract fun bindCitiesRepository(citiesRepositoryImpl: CitiesRepositoryImpl): CitiesRepository
}