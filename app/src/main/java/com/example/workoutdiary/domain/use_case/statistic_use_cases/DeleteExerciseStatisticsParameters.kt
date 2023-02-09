package com.example.workoutdiary.domain.use_case.statistic_use_cases

import com.example.workoutdiary.data.model.entities.ExerciseStatisticsParameters
import com.example.workoutdiary.domain.repository.StatisticsRepository

class DeleteExerciseStatisticsParameters(
    private val repository: StatisticsRepository
) {
    suspend operator fun invoke(statisticsParameters: ExerciseStatisticsParameters){
        repository.deleteExerciseStatisticsParameters(statisticsParameters)
    }
}