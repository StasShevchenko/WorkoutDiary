package com.example.workoutdiary.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class TrainingBlock(
    @PrimaryKey(autoGenerate = true)
    val trainingBlockId: Int,
    val trainingBlockOrder: Int,
    val trainingId: Int,
    val exerciseId: Int
)
