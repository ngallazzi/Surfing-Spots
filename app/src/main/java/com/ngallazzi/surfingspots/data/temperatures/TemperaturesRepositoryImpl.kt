package com.ngallazzi.surfingspots.data.temperatures

import com.ngallazzi.surfingspots.data.Result
import com.ngallazzi.surfingspots.data.temperatures.local.TemperaturesLocalDataSource
import com.ngallazzi.surfingspots.data.temperatures.remote.TemperaturesRemoteDataSource
import javax.inject.Inject

class TemperaturesRepositoryImpl @Inject constructor(
    private val localDataSource: TemperaturesLocalDataSource,
    private val remoteDataSource: TemperaturesRemoteDataSource
) : TemperaturesRepository {
    override suspend fun getRandomTemperature(forceUpdate: Boolean): Result<Int?> {
        return when (forceUpdate) {
            true -> remoteDataSource.getRandomTemperature()
            false -> localDataSource.getRandomTemperature()
        }
    }
}