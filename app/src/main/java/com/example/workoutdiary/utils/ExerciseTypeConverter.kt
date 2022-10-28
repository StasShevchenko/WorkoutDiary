package com.example.workoutdiary.utils

import androidx.room.TypeConverter

class ExerciseTypeConverter {
    @TypeConverter
    fun fromString(value: String): ExerciseType {
        return ExerciseType.getTypeByValue(value)
    }

    @TypeConverter
    fun typeToString(type: ExerciseType): String {
        return type.value
    }
}