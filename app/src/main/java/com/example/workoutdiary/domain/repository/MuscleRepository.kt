package com.example.workoutdiary.domain.repository

import com.example.workoutdiary.data.model.entities.Muscle
import kotlinx.coroutines.flow.Flow

interface MuscleRepository {
    fun getAllMuscles(): Flow<List<Muscle>>

    suspend fun insertMuscle(muscle: Muscle)

    suspend fun deleteMuscle(muscle: Muscle)
}