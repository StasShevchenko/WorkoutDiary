package com.example.workoutdiary.domain.use_case.trainings_use_cases

import com.example.workoutdiary.domain.repository.TrainingDetailsRepository
import com.example.workoutdiary.domain.repository.TrainingRepository

class DeleteTraining(
    private val trainingRepository: TrainingRepository,
    private val trainingDetailsRepository: TrainingDetailsRepository
) {
    suspend operator fun invoke(trainingId: Int){
        trainingRepository.deleteTraining(trainingId)
        trainingDetailsRepository.deleteOrderedSetsByTrainingId(trainingId)
        trainingDetailsRepository.deleteTrainingBlocksByTrainingId(trainingId)
    }
}