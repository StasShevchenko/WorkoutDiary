package com.example.workoutdiary.domain.use_case.exercise_use_cases

import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.domain.repository.ExerciseRepository

class AddExercise(
    private val repository: ExerciseRepository
) {
    suspend operator fun invoke(exercise: Exercise) {
        repository.insertExercise(exercise)
    }
}