package com.example.workoutdiary.domain.use_case

import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.domain.repository.TrainingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*

class GetTrainingsByMonth(
    private val trainingRepository: TrainingRepository
) {
    operator fun invoke(currentDate: Date): Flow<List<Training>> {
        return trainingRepository.getAllTrainings().map { trainings ->
            trainings.filter { training ->
                training.trainingDate.year == currentDate.year &&
                        training.trainingDate.month == currentDate.month
            }.sortedByDescending { training ->
               training.trainingDate.time
            }
        }
    }
}