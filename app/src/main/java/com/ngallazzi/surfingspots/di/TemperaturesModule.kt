package com.ngallazzi.surfingspots.di

import com.ngallazzi.surfingspots.data.temperatures.TemperaturesRepository
import com.ngallazzi.surfingspots.data.temperatures.TemperaturesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent


@Module
@InstallIn(ApplicationComponent::class)
abstract class TemperaturesModule {
    @Binds
    abstract fun bindTemperaturesRepository(temperatureRepositoryImpl: TemperaturesRepositoryImpl): TemperaturesRepository
}