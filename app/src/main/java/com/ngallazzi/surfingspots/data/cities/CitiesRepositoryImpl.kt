package com.ngallazzi.surfingspots.data.cities

import com.ngallazzi.surfingspots.Exceptions
import com.ngallazzi.surfingspots.data.Result
import com.ngallazzi.surfingspots.data.cities.local.CitiesLocalDataSource
import com.ngallazzi.surfingspots.data.cities.remote.CitiesRemoteDataSource
import com.ngallazzi.surfingspots.data.temperatures.TemperaturesRepository
import org.threeten.bp.LocalDateTime
import java.lang.Exception
import javax.inject.Inject


class CitiesRepositoryImpl @Inject constructor(
    private val localDataSource: CitiesLocalDataSource,
    private val remoteDataSource: CitiesRemoteDataSource,
    private val temperaturesRepository: TemperaturesRepository
) :
    CitiesRepository {
    override suspend fun getCities(forceUpdate: Boolean): Result<List<City>> {
        return when (forceUpdate) {
            true -> {
                when (val result = remoteDataSource.getCities()) {
                    is Result.Success -> {
                        result.data.apply {
                            assignRandomTemperatures(this)
                            assignImages(this)
                            localDataSource.insertCities(this, LocalDateTime.now())
                        }
                        result
                    }
                    is Result.Error -> {
                        Result.Error(Exception(Exceptions.SERVER_ERROR))
                    }
                }
            }
            false -> localDataSource.getCities()
        }
    }

    override suspend fun getLessRecentlyUpdatedCity(): City? {
        return localDataSource.getLessRecentlyUpdatedCity()
    }

    override suspend fun updateCityTemperature(cityName: String, temperature: Int) {
        localDataSource.updateCityTemperature(cityName, temperature, LocalDateTime.now())
    }

    override suspend fun getCityByName(cityName: String): City? {
        return localDataSource.getCityByName(cityName)
    }

    private suspend fun assignRandomTemperatures(cities: List<City>) {
        for (city in cities) {
            city.temperature = temperaturesRepository.getRandomTemperature(false)
        }
    }

    private fun assignImages(cities: List<City>) {
        for (city in cities) {
            when (city.name) {
                "Cuba" -> city.imageUrl =
                    "https://images.pexels.com/photos/1637122/pexels-photo-1637122.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"
                "Los Angeles" -> city.imageUrl =
                    "https://images.pexels.com/photos/1688186/pexels-photo-1688186.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"
                "Miami" -> city.imageUrl =
                    "https://images.pexels.com/photos/3663416/pexels-photo-3663416.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260"
                "Porto" -> city.imageUrl =
                    "https://images.pexels.com/photos/2549573/pexels-photo-2549573.jpeg?auto=compress&cs=tinysrgb&dpr=2&w=500"
                "Ortona" -> city.imageUrl =
                    "https://images.placesonline.com/photos/109806_ortona_ortona.jpg"
                "Riccione" -> city.imageUrl =
                    "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/14/10/2f/9a/riccione.jpg?w=1000&h=600&s=1"
                "Midgar" -> city.imageUrl =
                    "https://www.domusweb.it/content/dam/domusweb/en/architecture/gallery/2020/04/20/midgar-la-metropoli-dieselpunk-dove-lambiente-determina-lindividuo/ffviir-zoom-midgar-city%20(1).jpg.foto.rmedium.jpg"
                else -> city.imageUrl =
                    "https://fondazionefeltrinelli.it/app/uploads/2020/05/city.jpg"
            }
        }
    }
}