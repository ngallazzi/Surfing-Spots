package com.ngallazzi.surfingspots.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.ngallazzi.surfingspots.BuildConfig
import com.ngallazzi.surfingspots.data.cities.remote.CitiesApi
import com.ngallazzi.surfingspots.data.temperatures.remote.TemperaturesApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {
    private val loggingInterceptor by lazy {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        loggingInterceptor
    }

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun createCitiesInterface(): CitiesApi {
        val retrofit = getRetrofit(BuildConfig.CITY_NAMES_API_ENDPOINT, provideMoshi())
        return retrofit.create(CitiesApi::class.java)
    }

    @Provides
    @Singleton
    fun createTemperatureInterface(): TemperaturesApi {
        val retrofit = getRetrofit(BuildConfig.RANDOM_TEMPERATURES_API_ENDPOINT, provideMoshi())
        return retrofit.create(TemperaturesApi::class.java)
    }

    private fun getRetrofit(url: String, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(httpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        val moshiBuilder = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
        return moshiBuilder.build()
    }
}