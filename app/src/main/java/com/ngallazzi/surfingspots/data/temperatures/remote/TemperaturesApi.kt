package com.ngallazzi.surfingspots.data.temperatures.remote

import com.ngallazzi.surfingspots.data.cities.City
import retrofit2.Response
import retrofit2.http.GET

interface TemperaturesApi{
    @GET("math")
    suspend fun getRandomTemperature() : Response<String>
}