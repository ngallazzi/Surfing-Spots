package com.ngallazzi.surfingspots.data.cities.remote

import com.ngallazzi.surfingspots.Exceptions
import com.ngallazzi.surfingspots.data.Result
import com.ngallazzi.surfingspots.data.cities.CitiesDataSource
import com.ngallazzi.surfingspots.data.cities.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class CitiesRemoteDataSource @Inject constructor(private val service: CitiesApi) :
    CitiesDataSource {
    override suspend fun getCities(): Result<List<City>> = withContext(Dispatchers.IO) {
        try {
            val response = service.getCities()
            if (response.isSuccessful) {
                return@withContext Result.Success(response.body()!!.cities)
            } else {
                return@withContext Result.Error(Exception(Exceptions.SERVER_ERROR))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

}