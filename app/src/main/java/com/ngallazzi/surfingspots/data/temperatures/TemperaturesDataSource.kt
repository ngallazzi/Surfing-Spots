package com.ngallazzi.surfingspots.data.temperatures

import com.ngallazzi.surfingspots.data.Result

interface TemperaturesDataSource {
    suspend fun getRandomTemperature(): Result<Int?>
}