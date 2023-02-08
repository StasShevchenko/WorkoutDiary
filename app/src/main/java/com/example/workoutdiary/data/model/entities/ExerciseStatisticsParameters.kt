package com.example.workoutdiary.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ExerciseStatisticsParameters(
    @PrimaryKey
    val exerciseStatisticsParametersId: Int = 1,
    val exerciseName: String,
    val statisticsParameter: String
)