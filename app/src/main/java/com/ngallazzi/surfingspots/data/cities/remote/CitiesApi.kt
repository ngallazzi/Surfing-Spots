package com.ngallazzi.surfingspots.data.cities.remote

import retrofit2.Response
import retrofit2.http.GET

interface CitiesApi{
    @GET("652ceb94-b24e-432b-b6c5-8a54bc1226b6")
    suspend fun getCities() : Response<CitiesApiResponse>
}