package com.ngallazzi.surfingspots.data.cities

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

@Entity
data class City(@PrimaryKey val name: String,
                var imageUrl : String? = null,
                var temperature: Int? = null,
                var lastUpdate : LocalDateTime = LocalDateTime.MIN)