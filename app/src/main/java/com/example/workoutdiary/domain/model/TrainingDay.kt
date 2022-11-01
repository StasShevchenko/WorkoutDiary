package com.example.workoutdiary.domain.model

import com.example.workoutdiary.data.model.entities.Training
import java.time.LocalDate

data class TrainingDay(
    val date: LocalDate,
    val training: Training? = null
)
