package com.ngallazzi.surfingspots.data.cities

import androidx.lifecycle.LiveData
import com.ngallazzi.surfingspots.data.Result
import org.threeten.bp.LocalDateTime

class FakeDataSource(var cities: MutableList<City>? = mutableListOf()) : CitiesDataSource {
    override suspend fun getCities(): Result<List<City>> {
        cities?.let { return Result.Success(ArrayList(it)) }
        return Result.Error(
            Exception("Cities not found")
        )

    }

    override fun observeCities(): LiveData<Result<List<City>>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertCities(cities: List<City>, updateDate: LocalDateTime) {
        for (city in cities) {
            city.lastUpdate = updateDate
        }
        this.cities?.addAll(cities)
    }

    override suspend fun updateCityTemperature(
        cityName: String,
        temperature: Int,
        date: LocalDateTime
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun getLessRecentlyUpdatedCity(): City? {
        TODO("Not yet implemented")
    }

    override suspend fun getCityByName(name: String): City? {
        TODO("Not yet implemented")
    }
}