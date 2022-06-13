package com.example.workoutdiary.domain.use_case

import com.example.workoutdiary.domain.repository.TrainingDetailsRepository

class DeleteTrainingBlock(
    private val repository: TrainingDetailsRepository
) {
    suspend operator fun invoke(trainingBlockId: Int) {
        repository.deleteOrderedSets(trainingBlockId)
        repository.deleteTrainingBlock(trainingBlockId)
    }
}