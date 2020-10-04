package com.ngallazzi.surfingspots.data.cities.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
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

    override fun observeCities(): LiveData<Result<List<City>>> {
        return cityDao.observeCities().map {
            Result.Success(it)
        }
    }

    override suspend fun insertCities(cities: List<City>, date: LocalDateTime) = withContext(dispatcher) {
        for (city in cities) {
            city.lastUpdate = date
        }
        cityDao.insertCities(cities)
    }

    override suspend fun updateCityTemperature(cityName: String, temperature: Int, date: LocalDateTime) {
        cityDao.updateCityTemperature(cityName, temperature, date)
    }

    override suspend fun getLessRecentlyUpdatedCity(): City? {
        return cityDao.getCities().minByOrNull { it.lastUpdate }
    }

    override suspend fun getCityByName(name: String): City? {
        return cityDao.getCityByName(name)
    }
}