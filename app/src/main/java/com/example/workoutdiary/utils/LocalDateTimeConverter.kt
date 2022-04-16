package com.example.workoutdiary.utils

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.util.*

class LocalDateTimeConverter {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromString(value: String?): LocalDateTime? {
        return LocalDateTime.parse(value)
    }

    @TypeConverter
    fun dateToString(date: LocalDateTime?): String? {
        return date?.toString()
    }
}