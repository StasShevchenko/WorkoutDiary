package com.example.workoutdiary.domain.repository

import com.example.workoutdiary.data.model.entities.Exercise
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    fun getAllExercises(): Flow<List<Exercise>>

    suspend fun getExercisesByMuscleId(muscleId: Int): List<Exercise>

    suspend fun deleteExercise(exercise: Exercise)

    suspend fun insertExercise(exercise: Exercise)
}