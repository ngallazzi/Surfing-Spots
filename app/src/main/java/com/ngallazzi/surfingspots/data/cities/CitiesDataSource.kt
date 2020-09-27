package com.ngallazzi.surfingspots.data.cities

import com.ngallazzi.surfingspots.data.Result

interface CitiesDataSource {
    suspend fun getCities(): Result<List<City>>
}