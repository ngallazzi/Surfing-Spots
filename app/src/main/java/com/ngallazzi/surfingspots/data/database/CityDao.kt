package com.ngallazzi.surfingspots.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ngallazzi.surfingspots.data.cities.City
import org.threeten.bp.LocalDateTime

@Dao
interface CityDao {
    @Query("SELECT * FROM `City` ORDER BY temperature DESC")
    suspend fun getCities(): List<City>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<City>)

    @Query("UPDATE `City` SET temperature = :temperature, lastUpdate = :lastUpdate WHERE name = :name")
    suspend fun updateCityTemperature(
        name: String,
        temperature: Int,
        lastUpdate: LocalDateTime
    )

    @Query("DELETE FROM `City`")
    fun nukeTable()
}