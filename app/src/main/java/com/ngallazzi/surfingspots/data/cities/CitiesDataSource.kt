package com.ngallazzi.surfingspots.data.cities

import androidx.lifecycle.LiveData
import com.ngallazzi.surfingspots.data.Result
import org.threeten.bp.LocalDateTime

interface CitiesDataSource {
    suspend fun getCities(): Result<List<City>>

    fun observeCities(): LiveData<Result<List<City>>>

    suspend fun insertCities(cities : List<City>, updateDate : LocalDateTime)

    suspend fun updateCityTemperature(cityName: String, temperature: Int, date: LocalDateTime)

    suspend fun getLessRecentlyUpdatedCity() : City?

    suspend fun getCityByName(name : String) : City?
}