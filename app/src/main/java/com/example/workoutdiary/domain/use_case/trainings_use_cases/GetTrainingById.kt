package com.example.workoutdiary.domain.use_case.trainings_use_cases

import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.domain.repository.TrainingRepository

class GetTrainingById(
    private val repository: TrainingRepository
) {
    suspend operator fun invoke(trainingId: Int): Training {
        return repository.getTrainingById(trainingId)
    }
}