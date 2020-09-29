package com.ngallazzi.surfingspots.data.temperatures.local

import com.ngallazzi.surfingspots.data.temperatures.TemperaturesDataSource
import javax.inject.Inject

class TemperaturesLocalDataSource @Inject constructor() : TemperaturesDataSource {
    override suspend fun getRandomTemperature(): Int {
        return (1..40).shuffled().first()
    }
}