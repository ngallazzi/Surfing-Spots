package com.ngallazzi.surfingspots.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ngallazzi.surfingspots.data.cities.City
import org.threeten.bp.LocalDateTime

@Dao
interface CityDao {
    /**
     * Observes a list of cites.
     *
     * @return all cities ordered by temperature descending.
     */
    @Query("SELECT * FROM City ORDER BY temperature DESC")
    fun observeCities(): LiveData<List<City>>

    /**
     * Get a city by name.
     * @param name the city name.
     * @return the city with corresponding name.
     */
    @Query("SELECT * FROM `City` WHERE name = :name")
    suspend fun getCityByName(name: String): City?

    /**
     * Select all cities from the cities table.
     *
     * @return all cities ordered by temperature descending.
     */
    @Query("SELECT * FROM `City` ORDER BY temperature DESC")
    suspend fun getCities(): List<City>

    /**
     * Insert a set of cities in the cities table.
     * @param cities the list of cities to be inserted.
     * @return all cities ordered by temperature descending.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cities: List<City>)

    /**
     * Updates an existing city in the cities table.
     * @param name the name of city to update.
     * @param temperature the temperature of city to update.
     * @param lastUpdate the timestamp of the update.
     */
    @Query("UPDATE `City` SET temperature = :temperature, lastUpdate = :lastUpdate WHERE name = :name")
    suspend fun updateCityTemperature(
        name: String,
        temperature: Int,
        lastUpdate: LocalDateTime
    )

    /**
     * Delete all the entry from the city table
     */
    @Query("DELETE FROM `City`")
    fun nukeTable()
}