package com.example.workoutdiary.domain.use_case.statistic_use_cases

import com.example.workoutdiary.data.model.entities.ExerciseStatisticsParameters
import com.example.workoutdiary.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow

class GetExerciseStatisticsParameters(
    private val repository: StatisticsRepository
) {
     operator fun invoke(): Flow<ExerciseStatisticsParameters?>{
        return repository.getExerciseStatisticsParameters()
    }
}