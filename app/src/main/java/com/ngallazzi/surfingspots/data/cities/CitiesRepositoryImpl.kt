package com.ngallazzi.surfingspots.data.cities

import androidx.lifecycle.LiveData
import com.ngallazzi.surfingspots.data.Result
import com.ngallazzi.surfingspots.di.LocalDataSource
import com.ngallazzi.surfingspots.di.RemoteDataSource
import org.threeten.bp.LocalDateTime
import javax.inject.Inject
import kotlin.random.Random


class CitiesRepositoryImpl @Inject constructor(
    @LocalDataSource private val citiesLocalDataSource: CitiesDataSource,
    @RemoteDataSource private val citiesRemoteDataSource: CitiesDataSource
) :
    CitiesRepository {
    override suspend fun getCities(forceUpdate: Boolean): Result<List<City>> {
        return when (forceUpdate) {
            true -> {
                when (val result = citiesRemoteDataSource.getCities()) {
                    is Result.Success -> {
                        result.data.apply {
                            assignImages(this)
                            assignRandomTemperatures(this)
                            citiesLocalDataSource.insertCities(this, LocalDateTime.now())
                        }
                        result
                    }
                    is Result.Error -> {
                        Result.Error(result.exception)
                    }
                }
            }
            false -> citiesLocalDataSource.getCities()
        }
    }

    override fun observeCities(): LiveData<Result<List<City>>> {
        return citiesLocalDataSource.observeCities()
    }

    override suspend fun getLessRecentlyUpdatedCity(): City? {
        return citiesLocalDataSource.getLessRecentlyUpdatedCity()
    }

    override suspend fun updateCityTemperature(cityName: String, temperature: Int) {
        citiesLocalDataSource.updateCityTemperature(cityName, temperature, LocalDateTime.now())
    }

    override suspend fun getCityByName(cityName: String): City? {
        return citiesLocalDataSource.getCityByName(cityName)
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

    private fun assignRandomTemperatures(cities: List<City>) {
        for (city in cities) {
            city.temperature = Random.nextInt(RANDOM_TEMPERATURE_FROM, RANDOM_TEMPERATURE_TO)
        }
    }

    companion object {
        const val RANDOM_TEMPERATURE_FROM = -30
        const val RANDOM_TEMPERATURE_TO = 30
    }
}