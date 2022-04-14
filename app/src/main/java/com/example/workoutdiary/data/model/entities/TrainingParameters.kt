package com.example.workoutdiary.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class TrainingParameters(
    @PrimaryKey(autoGenerate = true)
    val trainingParametersId: Long,
    val repeats: Int? = null,
    val weight: Int? = null,
    val time: Int? = null,
    val distance: Int? = null
)
