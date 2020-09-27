package com.ngallazzi.surfingspots.data.database

import androidx.room.TypeConverter
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

class LocalDateTimeConverter {
    @TypeConverter
    fun toDate(timestamp: Int): LocalDateTime? {
        return try {
            LocalDateTime.ofInstant(
                Instant.ofEpochSecond(timestamp.toLong()),
                ZoneId.systemDefault()
            )
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun toTimeStamp(date: LocalDateTime?): Int? {
        return try {
            val zoneId =
                ZoneId.systemDefault() // or: ZoneId.of("Europe/Oslo");
            val epoch: Long = date!!.atZone(zoneId).toEpochSecond()
            epoch.toInt()
        } catch (e: Exception) {
            null
        }
    }
}