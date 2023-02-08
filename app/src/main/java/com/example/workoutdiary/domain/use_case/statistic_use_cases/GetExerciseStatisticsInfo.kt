package com.example.workoutdiary.domain.use_case.statistic_use_cases

import com.example.workoutdiary.domain.repository.StatisticsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.time.LocalDate

class GetExerciseStatisticsInfo(
    private val statisticsRepository: StatisticsRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
     operator fun invoke(): Flow<Pair<String, Map<LocalDate, Int>>> {
        return statisticsRepository.getExerciseStatisticsParameters().flatMapLatest { parameters ->
            if (parameters == null) {
                emptyFlow()
            } else{
                statisticsRepository.getExerciseStatisticsInfo(parameters.exerciseName, parameters.statisticsParameter).map {
                    parameters.exerciseName to it
                }
            }
        }
    }
}


