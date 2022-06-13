package com.example.workoutdiary.domain.use_case

import com.example.workoutdiary.domain.repository.MuscleRepository

class GetMuscles(
    private val muscleRepository: MuscleRepository
) {
    operator fun invoke(){
        muscleRepository.getAllMuscles()
    }
}