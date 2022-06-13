package com.example.workoutdiary.domain.use_case

import com.example.workoutdiary.data.model.entities.Muscle
import com.example.workoutdiary.domain.repository.MuscleRepository

class DeleteMuscle(
    private val repository: MuscleRepository
) {
    suspend operator fun invoke(muscle: Muscle){
        repository.deleteMuscle(muscle)
    }
}