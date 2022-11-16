package com.example.workoutdiary.domain.use_case.muscles_use_cases

import com.example.workoutdiary.data.model.entities.Muscle
import com.example.workoutdiary.domain.repository.MuscleRepository

class GetMuscleByName(
    private val repository: MuscleRepository
) {
    suspend operator fun invoke(muscleName: String): Muscle? {
        return repository.getMuscleByName(muscleName)
    }
}