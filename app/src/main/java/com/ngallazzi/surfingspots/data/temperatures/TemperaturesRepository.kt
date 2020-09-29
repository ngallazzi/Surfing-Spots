package com.ngallazzi.surfingspots.data.temperatures

interface TemperaturesRepository {
    suspend fun getRandomTemperature(forceUpdate: Boolean): Int?
}