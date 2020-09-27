package com.ngallazzi.surfingspots.data.temperatures

sealed class WeatherCondition {
    object Sunny : WeatherCondition()
    object Cloudy : WeatherCondition()

    companion object {
        fun getWeather(temperature: Int): WeatherCondition {
            return when (temperature >= 30) {
                true -> Sunny
                false -> Cloudy
            }
        }
    }
}

