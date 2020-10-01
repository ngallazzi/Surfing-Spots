package com.ngallazzi.surfingspots.data.cities

import androidx.lifecycle.LiveData
import com.ngallazzi.surfingspots.data.Result

interface CitiesDataSource {
    suspend fun getCities(): Result<List<City>>

    fun observeCities(): LiveData<Result<List<City>>>
}