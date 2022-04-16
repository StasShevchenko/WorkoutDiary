package com.example.workoutdiary.domain.use_case

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.domain.repository.TrainingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.util.*

class GetTrainingsByMonth(
    private val trainingRepository: TrainingRepository
) {
    @RequiresApi(Build.VERSION_CODES.O)
    operator fun invoke(currentDate: LocalDateTime): Flow<List<Training>> {
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