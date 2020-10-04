package com.ngallazzi.surfingspots.data.cities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ngallazzi.surfingspots.data.Result

class FakeCitiesRepository : CitiesRepository {

    private val cities = arrayListOf(
        City(
            "Miami",
            "https://ik.imagekit.io/grgdihc3l/Miami/media/Locations/Downtown%20Miami%20and%20Brickell/Downtown-Miami-views-1440x900.jpg?ext=.jpg",
            null
        ),
        City(
            "Cuba",
            "https://lh3.googleusercontent.com/proxy/x71TfP80E4_ihlR3ainTdKSxrcdM05FX6EBXbvXPk0iRfLxO_qcWyZHAQTc2pLjC6NsAW1LVVXx8bSmuJpSl1r8TVIv_g6fMsxBPiBX_HH6Ve9yfEus54xg",
            null
        ),
        City(
            "Puerto del Rosario",
            "https://www.tabularasateam.it/public/contenuti/dsc-2864.jpg",
            null
        ),
        City(
            "Vik",
            "https://i.ytimg.com/vi/_lH6Lj7Z280/maxresdefault.jpg",
            null
        )
    )

    override suspend fun getCities(forceUpdate: Boolean): Result<List<City>> {
        return Result.Success(cities)
    }

    override fun observeCities(): LiveData<Result<List<City>>> {
        val observableCities = MutableLiveData<Result<List<City>>>()
        observableCities.value = Result.Success(cities)
        return observableCities
    }

    override suspend fun getLessRecentlyUpdatedCity(): City? {
        return cities.last()
    }

    override suspend fun updateCityTemperature(cityName: String, temperature: Int) {
        cities.find { it.name == cityName }?.run {
            this.temperature = temperature
        }
    }

    override suspend fun getCityByName(cityName: String): City? {
        cities.find { it.name == cityName }?.run {
            return this
        }
        return null
    }

}