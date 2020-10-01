package com.ngallazzi.surfingspots.ui

import android.os.Looper
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
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

    private val _forceUpdate = MutableLiveData(false)

    private val temperaturesHandler = android.os.Handler(Looper.getMainLooper())

    init {
        scheduleTemperatureUpdates()
    }

    // everytime that _forceUpdate updates its value switchMap body is executed
    private val _cities: LiveData<List<City>> = _forceUpdate.switchMap { forceUpdate ->
        if (forceUpdate) {
            _dataLoading.value = true
            viewModelScope.launch {
                citiesRepository.getCities(true)
                _dataLoading.value = false
            }
        }
        citiesRepository.observeCities().switchMap { filterCities(it) }
    }

    val cities: LiveData<List<City>> = _cities

    private fun scheduleTemperatureUpdates(periodInSeconds: Long = UPDATES_PERIOD) {
        val runnableCode: Runnable = object : Runnable {
            override fun run() {
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
                                    Timber.v("Updated city: ${this.name}, newTemperature: $temperatureUpdate")
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

    fun loadCities(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
    }

    private fun filterCities(citiesResult: Result<List<City>>): LiveData<List<City>> {
        val result = MutableLiveData<List<City>>()

        if (citiesResult is Result.Success) {
            viewModelScope.launch {
                result.value = citiesResult.data!!
            }
        } else if (citiesResult is Result.Error) {
            result.value = emptyList()
            _error.postValue(citiesResult.exception.message)
        }

        return result

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