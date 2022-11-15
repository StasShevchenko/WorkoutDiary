package com.example.workoutdiary.domain.use_case.exercise_use_cases

import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow

class GetExercises(
    private val repository: ExerciseRepository
) {
    operator fun invoke(): Flow<List<Exercise>> {
        return repository.getAllExercises()
    }
}