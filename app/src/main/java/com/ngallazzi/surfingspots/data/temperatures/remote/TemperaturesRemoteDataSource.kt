package com.ngallazzi.surfingspots.data.temperatures.remote

import com.ngallazzi.surfingspots.data.temperatures.TemperaturesDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class TemperaturesRemoteDataSource @Inject constructor(private val service: TemperaturesApi) :
    TemperaturesDataSource {
    override suspend fun getRandomTemperature(): Int? = withContext(Dispatchers.IO) {
        try {
            val response = service.getRandomTemperature()
            when (response.isSuccessful){
                true -> {
                    service.getRandomTemperature()
                    return@withContext parseResponse(response.body().toString())
                }
                else -> return@withContext null
            }

        } catch (e: Exception) {
            Timber.d(e)
            return@withContext null
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