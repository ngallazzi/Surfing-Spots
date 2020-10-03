package com.ngallazzi.surfingspots.di

import android.os.Looper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Module
@InstallIn(ApplicationComponent::class)
object LooperModule {
    @MainLooper
    @Provides
    fun providesMainLooper(): Looper = Looper.getMainLooper()
}

@Retention(AnnotationRetention.BINARY)
@Qualifier
annotation class MainLooper