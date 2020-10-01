package com.ngallazzi.surfingspots.data.cities

import androidx.lifecycle.LiveData
import com.ngallazzi.surfingspots.data.Result

interface CitiesRepository {
    suspend fun getCities(forceUpdate: Boolean): Result<List<City>>

    fun observeCities(): LiveData<Result<List<City>>>

    suspend fun getLessRecentlyUpdatedCity(): City?

    suspend fun updateCityTemperature(cityName: String, temperature: Int)

    suspend fun getCityByName(cityName: String): City?
}