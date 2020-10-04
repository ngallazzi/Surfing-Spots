package com.ngallazzi.surfingspots.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ngallazzi.surfingspots.MainCoroutineRule
import com.ngallazzi.surfingspots.data.cities.FakeCitiesRepository
import com.ngallazzi.surfingspots.data.cities.FakeTemperaturesRepository
import com.ngallazzi.surfingspots.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class ForecastsViewModelTest {
    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    // Subject under test
    private lateinit var forecastsViewModel: ForecastsViewModel

    // Use fake repositories to be injected into the view model.
    private lateinit var citiesRepository: FakeCitiesRepository
    private lateinit var temperaturesRepository: FakeTemperaturesRepository

    @Before
    fun setup() {
        // Initialise the repository with no tasks.
        citiesRepository = FakeCitiesRepository()
        temperaturesRepository = FakeTemperaturesRepository()
    }

    @Test
    fun forcingLoadCities_triggersLoading() {
        forecastsViewModel =
            ForecastsViewModel(citiesRepository, temperaturesRepository)
        // pausing dispatchers all coroutines will be added to queue instead of running
        mainCoroutineRule.pauseDispatcher()
        forecastsViewModel.loadCities(true)
        MatcherAssert.assertThat(
            forecastsViewModel.dataLoading.getOrAwaitValue(),
            CoreMatchers.`is`(true)
        )
        //resuming dispatcher, data loading should be finished now
        mainCoroutineRule.resumeDispatcher()
        MatcherAssert.assertThat(
            forecastsViewModel.dataLoading.getOrAwaitValue(),
            CoreMatchers.`is`(false)
        )
    }
}