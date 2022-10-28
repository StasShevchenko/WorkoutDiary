package com.example.workoutdiary.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.workoutdiary.utils.ExerciseType


@Entity
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val exerciseId: Int,
    val exerciseName: String,
    val exerciseType: ExerciseType,
    val exerciseDescription: String? = null,
    val muscleId: Int
){
    override fun toString(): String {
        return this.exerciseName
    }
}
