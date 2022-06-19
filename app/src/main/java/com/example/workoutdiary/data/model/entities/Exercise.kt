package com.example.workoutdiary.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val exerciseId: Int,
    val exerciseName: String,
    val exerciseType: String,
    val exerciseDescription: String? = null,
    val muscleId: Int
){
    override fun toString(): String {
        return this.exerciseName
    }
}
