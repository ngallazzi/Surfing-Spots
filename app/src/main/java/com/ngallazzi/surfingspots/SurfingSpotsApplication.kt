package com.ngallazzi.surfingspots

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.ngallazzi.surfingspots.data.cities.CitiesRepository
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class SurfingSpotsApplication : Application(){
    @Inject lateinit var citiesRepository: CitiesRepository

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        Timber.plant(Timber.DebugTree())
    }
}
