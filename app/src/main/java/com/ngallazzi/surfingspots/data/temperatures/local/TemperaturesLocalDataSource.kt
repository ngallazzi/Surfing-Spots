package com.ngallazzi.surfingspots.data.temperatures.local

import com.ngallazzi.surfingspots.data.Result
import com.ngallazzi.surfingspots.data.temperatures.TemperaturesDataSource
import javax.inject.Inject

class TemperaturesLocalDataSource @Inject constructor() : TemperaturesDataSource {
    override suspend fun getRandomTemperature(): Result<Int?> {
        return Result.Success((1..40).shuffled().first())
    }
}