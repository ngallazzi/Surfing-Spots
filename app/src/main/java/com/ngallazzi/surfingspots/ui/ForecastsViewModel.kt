package com.ngallazzi.surfingspots.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ngallazzi.surfingspots.data.Result
import com.ngallazzi.surfingspots.data.cities.CitiesRepository
import com.ngallazzi.surfingspots.data.cities.City
import kotlinx.coroutines.launch

class ForecastsViewModel @ViewModelInject constructor(
    private val citiesRepository: CitiesRepository
) : ViewModel() {
    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _cities = MutableLiveData<List<City>>()
    val cities: LiveData<List<City>> = _cities

    fun getCities(forceUpdate: Boolean) {
        viewModelScope.launch {
            _dataLoading.postValue(true)
            when (val result = citiesRepository.getCities(forceUpdate)) {
                is Result.Success -> {
                    result.data.let { cityList ->
                        _cities.postValue(cityList.sortedByDescending { it.temperature })
                    }
                }
                is Result.Error -> {
                    _error.postValue(result.exception.message)
                }
            }
            _dataLoading.postValue(false)
        }
    }
}