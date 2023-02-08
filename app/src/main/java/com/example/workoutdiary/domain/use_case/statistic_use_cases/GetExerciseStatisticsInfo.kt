package com.example.workoutdiary.domain.use_case.statistic_use_cases

import com.example.workoutdiary.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class GetExerciseStatisticsInfo(
    private val statisticsRepository: StatisticsRepository
) {
    suspend operator fun invoke(): Flow<Pair<String, Map<LocalDate, Int>>>? {
        val exerciseStatisticsParameters = statisticsRepository.getExerciseStatisticsParameters()
            ?: return null
        return statisticsRepository.getExerciseStatisticsInfo(
            exerciseStatisticsParameters.exerciseName,
            exerciseStatisticsParameters.statisticsParameter
        ).map { statisticsValue ->
            Pair(exerciseStatisticsParameters.exerciseName, statisticsValue)
        }
    }
}