package com.example.workoutdiary.domain.use_case

import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.domain.repository.ExerciseRepository

class GetExercise(
    private val exerciseRepository: ExerciseRepository
) {
    suspend operator fun invoke(exerciseId: Int): Exercise {
       return exerciseRepository.getExerciseById(exerciseId)
    }
}