package com.example.workoutdiary.domain.use_case.muscles_use_cases

import com.example.workoutdiary.data.model.entities.Muscle
import com.example.workoutdiary.domain.repository.MuscleRepository

class AddMuscle(
    private val repository: MuscleRepository
) {
    suspend operator fun invoke(muscle: Muscle) {
        repository.insertMuscle(muscle)
    }
}