package com.ngallazzi.surfingspots.data.temperatures

interface TemperaturesDataSource {
    suspend fun getRandomTemperature(): Int?
}