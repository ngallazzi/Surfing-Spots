package com.ngallazzi.surfingspots.data.temperatures

import com.ngallazzi.surfingspots.data.Result

interface TemperaturesRepository {
    suspend fun getRandomTemperature(forceUpdate: Boolean): Result<Int?>
}