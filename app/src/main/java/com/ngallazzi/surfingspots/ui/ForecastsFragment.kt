package com.ngallazzi.surfingspots.ui

import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var cityAdapter: CityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forecastsViewModel.loadCities(true)
        cityAdapter = CityAdapter(requireContext())
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
        forecastsViewModel.dataLoading.observe(viewLifecycleOwner, { loading ->
            when (loading) {
                true -> LayoutUtils.crossFade(pbLoading, rvCitiesForecasts)
                false -> LayoutUtils.crossFade(rvCitiesForecasts, pbLoading)
            }
        })

        forecastsViewModel.cities.observe(viewLifecycleOwner, {
            cityAdapter.submitUpdate(it)
        })

        forecastsViewModel.citiesDownloaded.observe(viewLifecycleOwner, {
            forecastsViewModel.scheduleTemperatureUpdates(looper = Looper.getMainLooper())
        })

        rvCitiesForecasts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = cityAdapter
        }

        forecastsViewModel.error.observe(viewLifecycleOwner, {
            Toast.makeText(
                requireContext(),
                getString(R.string.an_error_has_occurred, it),
                Toast.LENGTH_SHORT
            ).show()
        })
    }
}