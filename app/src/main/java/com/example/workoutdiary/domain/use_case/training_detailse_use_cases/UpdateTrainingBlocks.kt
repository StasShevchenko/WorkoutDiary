package com.example.workoutdiary.domain.use_case.training_detailse_use_cases

import com.example.workoutdiary.data.model.entities.TrainingBlock
import com.example.workoutdiary.domain.repository.TrainingRepository

class UpdateTrainingBlocks(
    private val repository: TrainingRepository
) {
    suspend operator fun invoke(trainingBlocks: List<TrainingBlock>) {
        repository.updateTrainingBlocks(trainingBlocks)
    }
}