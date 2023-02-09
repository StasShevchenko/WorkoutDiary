package com.example.workoutdiary.domain.use_case.trainings_use_cases

import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.domain.repository.TrainingRepository

class InsertTraining(
    private val trainingRepository: TrainingRepository
) {
    suspend operator fun invoke(training: Training): Long {
        return trainingRepository.insertTraining(training)
    }
}