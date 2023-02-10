package com.example.workoutdiary.domain.use_case.trainings_use_cases

import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.domain.repository.TrainingRepository
import kotlinx.coroutines.flow.Flow

class GetAllFavouriteTrainings(
    private val repository: TrainingRepository
) {
    suspend operator fun invoke(): Flow<List<Training>> {
        return repository.getAllFavouritesTrainings()
    }
}