package com.example.workoutdiary.domain.use_case.trainings_use_cases

import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.domain.repository.TrainingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class GetTrainingsByMonth(
    private val trainingRepository: TrainingRepository
) {
    operator fun invoke(currentDate: LocalDate): Flow<List<Training>> {
        return trainingRepository.getAllTrainings().map { trainings ->
            trainings.filter { training ->
                training.trainingDate.year == currentDate.year &&
                        training.trainingDate.month == currentDate.month
            }.sortedByDescending { training ->
               training.trainingDate
            }
        }
    }
}