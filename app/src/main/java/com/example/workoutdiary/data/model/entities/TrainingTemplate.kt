package com.example.workoutdiary.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class TrainingTemplate(
    @PrimaryKey(autoGenerate = true)
    val templateId: Int,
    val templateName: String,
    val trainingId: Int
)