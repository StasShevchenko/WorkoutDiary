package com.example.workoutdiary.domain.use_case

import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.domain.repository.TrainingDetailsRepository
import com.example.workoutdiary.domain.repository.TrainingRepository

class DeleteTraining(
    private val trainingRepository: TrainingRepository,
    private val trainingDetailsRepository: TrainingDetailsRepository
) {
    suspend operator fun invoke(trainingId: Int){
        trainingRepository.deleteTraining(trainingId)
        trainingDetailsRepository.deleteTrainingBlocksByTrainingId(trainingId)
        trainingDetailsRepository.deleteOrderedSetsByTrainingId(trainingId)
    }
}