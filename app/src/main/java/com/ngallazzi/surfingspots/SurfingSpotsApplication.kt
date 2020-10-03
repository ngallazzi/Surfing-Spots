package com.ngallazzi.surfingspots

import android.app.Application
import android.os.Looper
import com.jakewharton.threetenabp.AndroidThreeTen
import com.ngallazzi.surfingspots.data.cities.CitiesRepository
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class SurfingSpotsApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        Timber.plant(Timber.DebugTree())
    }
}
