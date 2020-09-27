package com.ngallazzi.surfingspots.data.cities.local

import com.ngallazzi.surfingspots.Exceptions
import com.ngallazzi.surfingspots.data.Result
import com.ngallazzi.surfingspots.data.cities.CitiesDataSource
import com.ngallazzi.surfingspots.data.cities.City
import com.ngallazzi.surfingspots.data.database.CityDao
import com.ngallazzi.surfingspots.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class CitiesLocalDataSource @Inject constructor(
    private val cityDao: CityDao,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : CitiesDataSource {

    override suspend fun getCities(): Result<List<City>> = withContext(dispatcher) {
        val cities = cityDao.getCities()
        return@withContext when (cities.size) {
            0 -> Result.Error(Exception(Exceptions.NO_CITIES_FOUND))
            else -> Result.Success(cities)
        }
    }

    suspend fun insertCities(cities: List<City>, date: LocalDateTime) = withContext(dispatcher) {
        for (city in cities) {
            city.lastUpdate = date
        }
        cityDao.insertCities(cities)
    }

    suspend fun updateCityTemperature(name: String, temperature: Int) {
        cityDao.updateCityTemperature(name, temperature, LocalDateTime.now())
    }
}