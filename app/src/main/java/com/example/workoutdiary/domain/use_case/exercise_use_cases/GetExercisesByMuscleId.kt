package com.example.workoutdiary.domain.use_case.exercise_use_cases

import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.domain.repository.ExerciseRepository

class GetExercisesByMuscleId(
    private val repository: ExerciseRepository
) {
    suspend operator fun invoke(muscleId: Int): List<Exercise> {
        return repository.getExercisesByMuscleId(muscleId)
    }
}