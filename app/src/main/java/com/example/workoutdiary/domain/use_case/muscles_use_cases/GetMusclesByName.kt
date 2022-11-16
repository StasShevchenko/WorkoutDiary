package com.example.workoutdiary.domain.use_case.muscles_use_cases

import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.data.model.entities.Muscle
import com.example.workoutdiary.domain.repository.MuscleRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMusclesByName(
    private val repository: MuscleRepository
) {
    operator fun invoke(muscleName: String): Flow<List<Muscle>> {
        return repository.getAllMuscles().map { muscles ->
            if (muscleName.isEmpty()) {
                muscles
            } else{
                muscles.filter { muscle ->
                    muscle.muscleName.contains(muscleName, ignoreCase = true)
                }
            }
        }
    }
}


