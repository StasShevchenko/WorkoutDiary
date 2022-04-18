package com.example.workoutdiary.domain.model

import com.example.workoutdiary.data.model.entities.Training
import java.time.LocalDate
import java.time.LocalDateTime

data class TrainingDay(
    val date: LocalDate,
    val trainingList: MutableList<Training> = mutableListOf<Training>()
)
