package com.ngallazzi.surfingspots.ui

import android.os.Looper
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ngallazzi.surfingspots.data.Result
import com.ngallazzi.surfingspots.data.cities.CitiesRepository
import com.ngallazzi.surfingspots.data.cities.City
import com.ngallazzi.surfingspots.data.temperatures.TemperaturesRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ForecastsViewModel @ViewModelInject constructor(
    private val citiesRepository: CitiesRepository,
    private val temperaturesRepository: TemperaturesRepository
) : ViewModel() {
    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _lastUpdatedCity = MutableLiveData<City>()
    val lastUpdatedCity: LiveData<City> = _lastUpdatedCity

    private val _cities = MutableLiveData<List<City>>()
    val cities: LiveData<List<City>> = _cities

    private val temperaturesHandler = android.os.Handler(Looper.getMainLooper())
    private var temperaturesHandlerActive = false


    fun getCities(forceUpdate: Boolean) {
        viewModelScope.launch {
            _dataLoading.postValue(true)
            when (val result = citiesRepository.getCities(forceUpdate)) {
                is Result.Success -> {
                    result.data.let { cityList ->
                        _cities.postValue(cityList.sortedByDescending { it.temperature })
                        if (!temperaturesHandlerActive) {
                            scheduleTemperatureUpdates()
                        }
                    }
                }
                is Result.Error -> {
                    _error.postValue(result.exception.message)
                }
            }
            _dataLoading.postValue(false)
        }
    }

    private fun scheduleTemperatureUpdates(periodInSeconds: Long = UPDATES_PERIOD) {
        val runnableCode: Runnable = object : Runnable {
            override fun run() {
                temperaturesHandlerActive = true
                // Do something here on the main thread, we can call api since it's main safe
                runBlocking {
                    when (val result = temperaturesRepository.getRandomTemperature(true)) {
                        is Result.Success -> {
                            Timber.v("Random temperature fetched: ${result.data}")
                            result.data?.let { temperatureUpdate ->
                                citiesRepository.getLessRecentlyUpdatedCity()?.run {
                                    citiesRepository.updateCityTemperature(
                                        this.name,
                                        temperatureUpdate
                                    )
                                    citiesRepository.getCityByName(this.name)?.run {
                                        postTemperatureUpdate(this)
                                    }
                                }
                            }
                        }
                        is Result.Error -> {
                            _error.postValue(result.exception.message)
                        }
                    }
                }
                temperaturesHandler.postDelayed(this, periodInSeconds)
            }
        }
        // Starts the initial runnable task by posting through the handler
        temperaturesHandler.post(runnableCode)
    }

    private fun postTemperatureUpdate(updatedCity: City) {
        _cities.value?.find { it.name == updatedCity.name }?.run {
            this.temperature = updatedCity.temperature
            this.lastUpdate = updatedCity.lastUpdate
            _lastUpdatedCity.postValue(this)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // stops scheduled temperature updates
        temperaturesHandler.removeCallbacksAndMessages(null)
    }

    companion object {
        val UPDATES_PERIOD = TimeUnit.SECONDS.toMillis(3)
    }
}