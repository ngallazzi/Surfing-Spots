package com.ngallazzi.surfingspots.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.ngallazzi.surfingspots.R
import com.ngallazzi.surfingspots.data.cities.City
import com.ngallazzi.surfingspots.ui.adapters.CityAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_forecasts.*
import timber.log.Timber

@AndroidEntryPoint
class ForecastsFragment : Fragment() {
    private val forecastsViewModel: ForecastsViewModel by viewModels()
    private val cities: ArrayList<City> = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forecastsViewModel.getCities(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forecasts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        forecastsViewModel.dataLoading.observe(viewLifecycleOwner, {
            Timber.v("Loading: $it")
        })

        forecastsViewModel.cities.observe(viewLifecycleOwner, {
            cities.clear()
            cities.addAll(it)
            rvCitiesForecasts.adapter?.notifyDataSetChanged()
        })

        rvCitiesForecasts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = CityAdapter(cities, requireContext())
        }

        forecastsViewModel.lastUpdatedCity.observe(viewLifecycleOwner, { city ->
            cities.indexOf(city).let { index ->
                cities[index].temperature = city.temperature
                Timber.v("Updated city: ${city.name}, temperature: ${city.temperature}")
                cities.sortByDescending { it.temperature }
                rvCitiesForecasts.adapter?.notifyDataSetChanged()
            }
        })

        forecastsViewModel.error.observe(viewLifecycleOwner, {
            Snackbar.make(
                clForecastContainer,
                getString(R.string.an_error_has_occurred, it),
                Snackbar.LENGTH_SHORT
            ).show()
        })
    }
}