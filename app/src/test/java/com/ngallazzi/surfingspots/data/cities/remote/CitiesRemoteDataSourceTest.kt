package com.ngallazzi.surfingspots.data.cities.remote

import com.ngallazzi.surfingspots.data.Result
import com.ngallazzi.surfingspots.data.cities.City
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class CitiesRemoteDataSourceTest {
    private lateinit var citiesRemoteDataSource: CitiesRemoteDataSource
    private val testDispatcher = TestCoroutineDispatcher()

    class SuccessfulCitiesApi : CitiesApi {
        override suspend fun getCities(): Response<List<City>> {
            return Response.success(
                listOf(
                    City("Chicago", 30),
                    City("Miami", 3),
                    City("Torino", 40)
                )
            )
        }
    }

    class UnsuccessfulCityApi : CitiesApi {
        override suspend fun getCities(): Response<List<City>> {
            return Response.error(500, ResponseBody.create(null, "generic error"))
        }
    }

    @Before
    fun setup() {
        // Sets the given [dispatcher] as an underlying dispatcher of [Dispatchers.Main].
        // All consecutive usages of [Dispatchers.Main] will use given [dispatcher] under the hood.
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun testSuccessfulApi() = runBlocking {
        citiesRemoteDataSource = CitiesRemoteDataSource(SuccessfulCitiesApi())
        val response = citiesRemoteDataSource.getCities()
        assert(response is Result.Success)
    }

    @Test
    fun testUnsuccessfulApi() = runBlocking {
        citiesRemoteDataSource = CitiesRemoteDataSource(UnsuccessfulCityApi())
        val response = citiesRemoteDataSource.getCities()
        assert(response is Result.Error)
    }
}