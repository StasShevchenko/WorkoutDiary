package com.example.workoutdiary.domain.use_case.training_detailse_use_cases

import com.example.workoutdiary.domain.repository.TrainingDetailsRepository

class DeleteTrainingBlock(
    private val repository: TrainingDetailsRepository
) {
    suspend operator fun invoke(trainingBlockId: Int) {
        repository.deleteOrderedSets(trainingBlockId)
        repository.deleteTrainingBlock(trainingBlockId)
    }
}