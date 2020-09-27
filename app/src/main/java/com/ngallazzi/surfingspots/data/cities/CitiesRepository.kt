package com.ngallazzi.surfingspots.data.cities

import com.ngallazzi.surfingspots.data.Result

interface CitiesRepository {
    suspend fun getCities(forceUpdate: Boolean): Result<List<City>>
}