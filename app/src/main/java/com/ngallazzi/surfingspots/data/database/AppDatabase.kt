package com.ngallazzi.surfingspots.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ngallazzi.surfingspots.data.cities.City

@Database(entities = [City::class], version = 3, exportSchema = false)

@TypeConverters(
    LocalDateTimeConverter::class)

abstract class AppDatabase : RoomDatabase(){
    abstract fun cityDao() : CityDao
}