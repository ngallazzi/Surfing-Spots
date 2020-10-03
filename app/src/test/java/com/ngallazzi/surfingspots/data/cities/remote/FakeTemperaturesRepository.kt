package com.ngallazzi.surfingspots.data.cities.remote

import com.ngallazzi.surfingspots.data.Result
import com.ngallazzi.surfingspots.data.temperatures.TemperaturesRepository
import kotlin.random.Random

class FakeTemperaturesRepository : TemperaturesRepository {
    override suspend fun getRandomTemperature(forceUpdate: Boolean): Result<Int?> {
        return Result.Success(Random.nextInt(RANDOM_TEMPERATURE_FROM, RANDOM_TEMPERATURE_TO))
    }


    companion object {
        const val RANDOM_TEMPERATURE_FROM = -30
        const val RANDOM_TEMPERATURE_TO = 30
    }
}