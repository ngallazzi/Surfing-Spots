package com.ngallazzi.surfingspots.data.cities.remote

import com.ngallazzi.surfingspots.data.cities.City
import com.squareup.moshi.Json

class CitiesApiResponse(@field:Json(name = "cities") val cities: List<City>)