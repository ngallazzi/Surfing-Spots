package com.ngallazzi.surfingspots.data.cities.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val observableCities = MutableLiveData<Result<List<City>>>()

    override suspend fun getCities(): Result<List<City>> = withContext(Dispatchers.IO) {
        try {
            val response = service.getCities()
            if (response.isSuccessful) {
                val result = Result.Success(response.body()!!.cities)
                observableCities.postValue(result)
                return@withContext result
            } else {
                return@withContext Result.Error(Exception(Exceptions.SERVER_ERROR))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    override fun observeCities(): LiveData<Result<List<City>>> {
        return observableCities
    }

}