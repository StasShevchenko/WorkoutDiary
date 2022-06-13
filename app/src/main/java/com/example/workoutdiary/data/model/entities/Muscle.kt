package com.example.workoutdiary.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Muscle(
    @PrimaryKey(autoGenerate = true)
    val muscleId: Int,
    val muscleName: String
){
    override fun toString(): String {
        return this.muscleName
    }
}
