package com.example.workoutdiary.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate


@Entity
data class Training(
    @PrimaryKey(autoGenerate = true)
    val trainingId: Int,
    val trainingName: String,
    val trainingDate: LocalDate,
    val isFavourite: Boolean = false
)
