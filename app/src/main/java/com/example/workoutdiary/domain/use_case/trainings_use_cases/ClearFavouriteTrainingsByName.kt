package com.example.workoutdiary.domain.use_case.trainings_use_cases

import com.example.workoutdiary.domain.repository.TrainingRepository

class ClearFavouriteTrainingsByName(
    private val repository: TrainingRepository
) {
    suspend operator fun invoke(trainingName: String) {
        repository.clearFavouriteTrainingsByName(trainingName)
    }
}