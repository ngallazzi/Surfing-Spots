package com.ngallazzi.surfingspots.data.temperatures.remote

import com.ngallazzi.surfingspots.Exceptions
import com.ngallazzi.surfingspots.data.Result
import com.ngallazzi.surfingspots.data.temperatures.TemperaturesDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class TemperaturesRemoteDataSource @Inject constructor(private val service: TemperaturesApi) :
    TemperaturesDataSource {
    override suspend fun getRandomTemperature(): Result<Int?> = withContext(Dispatchers.IO) {
        try {
            val response = service.getRandomTemperature()
            when (response.isSuccessful) {
                true -> {
                    return@withContext Result.Success(parseResponse(response.body().toString()))
                }
                else -> return@withContext Result.Error(Exception(Exceptions.UNABLE_TO_FETCH_TEMPERATURES))
            }

        } catch (e: Exception) {
            Timber.d(e)
            return@withContext Result.Error(e)
        }
    }

    private fun parseResponse(response: String): Int? {
        return try {
            response.split(" ").first().toInt()
        } catch (e: Exception) {
            null
        }
    }
}