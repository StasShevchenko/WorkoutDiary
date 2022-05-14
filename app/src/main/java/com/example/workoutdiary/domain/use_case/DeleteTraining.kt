package com.example.workoutdiary.domain.use_case

import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.domain.repository.TrainingRepository

class DeleteTraining(
    private val trainingRepository: TrainingRepository
) {
    suspend operator fun invoke(trainingId: Int){
        trainingRepository.deleteTraining(trainingId)
    }
}