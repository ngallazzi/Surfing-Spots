package com.ngallazzi.surfingspots.data.cities

import com.ngallazzi.surfingspots.MainCoroutineRule
import com.ngallazzi.surfingspots.data.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CitiesRepositoryImplTest {
    private val cuba = City("Cuba", "")
    private val miami = City("Miami", "", 33)
    private val midgar = City("Midga", "", 44)
    private val remoteCities = listOf(cuba)
    private val localCities = listOf(miami, midgar)

    private lateinit var citiesRemoteDataSource: FakeDataSource
    private lateinit var citiesLocalDataSource: FakeDataSource

    // class under tests
    private lateinit var citiesRepository: CitiesRepository

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun createRepository() {
        citiesRemoteDataSource = FakeDataSource(remoteCities.toMutableList())
        citiesLocalDataSource = FakeDataSource(localCities.toMutableList())
        citiesRepository = CitiesRepositoryImpl(citiesLocalDataSource, citiesRemoteDataSource)
    }

    @Test
    fun getCities_requestCitiesFromRemoteDataSource() =
        // When cities are requested from the cities repository with a force update
        mainCoroutineRule.runBlockingTest {
            val cities = citiesRepository.getCities(true) as Result.Success
            // Then cities are loaded from the remote data source
            Assert.assertThat(cities.data, IsEqual(remoteCities))
        }

    @Test
    fun getCities_requestCitiesFromLocalDataSource() =
        // When cities are requested from the cities repository without a force update
        mainCoroutineRule.runBlockingTest {
            val cities = citiesRepository.getCities(false) as Result.Success
            // Then cities are loaded from the remote data source
            Assert.assertThat(cities.data, IsEqual(localCities))
        }
}