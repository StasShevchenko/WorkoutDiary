package com.example.workoutdiary.domain.use_case

import com.example.workoutdiary.data.model.entities.Muscle
import com.example.workoutdiary.domain.repository.MuscleRepository
import kotlinx.coroutines.flow.Flow

class GetMuscles(
    private val muscleRepository: MuscleRepository
) {
    operator fun invoke(): Flow<List<Muscle>> {
        return muscleRepository.getAllMuscles()
    }
}