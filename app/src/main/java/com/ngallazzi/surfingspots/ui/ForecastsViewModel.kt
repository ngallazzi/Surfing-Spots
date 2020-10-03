package com.ngallazzi.surfingspots.ui

import android.os.Handler
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

    private val _dataLoading = MutableLiveData(false)
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _citiesDownloaded = MutableLiveData<Boolean>()
    val citiesDownloaded = _citiesDownloaded

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _forceUpdate = MutableLiveData(false)

    // switch map transforms LiveData<Result<List<City>>> into LiveData<List<City>> thanks to filterCities function
    private val _cities: LiveData<List<City>> =  citiesRepository.observeCities().switchMap { filterCities(it) }

    val cities: LiveData<List<City>> = _cities

    fun scheduleTemperatureUpdates(periodInSeconds: Long = UPDATES_PERIOD, looper: Looper) {
        val temperaturesHandler = Handler(looper)
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
        // Starts the initial runnable task by posting through the handler, delayed by 5 seconds
        temperaturesHandler.postDelayed(runnableCode, INITIAL_DELAY)
    }

    fun loadCities(forceUpdate: Boolean) {
        _forceUpdate.value = forceUpdate
        if (forceUpdate) {
            _dataLoading.value = true
            viewModelScope.launch {
                when (val result = citiesRepository.getCities(true)) {
                    is Result.Success -> {
                        citiesDownloaded.postValue(true)
                    }
                    is Result.Error -> {
                        _error.postValue(result.exception.message)
                    }
                }
                _dataLoading.value = false
            }
        }
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

    companion object {
        val UPDATES_PERIOD = TimeUnit.SECONDS.toMillis(3)
        val INITIAL_DELAY = TimeUnit.SECONDS.toMillis(5)
    }
}