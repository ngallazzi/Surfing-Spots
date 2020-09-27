package com.ngallazzi.surfingspots.data.temperatures

interface TemperaturesDataSource {
    fun getRandomTemperature(): Int?
}