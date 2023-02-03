package com.example.workoutdiary.domain.use_case.statistic_use_cases

import com.example.workoutdiary.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow

class GetTotalRepsCount(
    private val repository: StatisticsRepository
) {
    operator fun invoke(): Flow<Int?> {
        return repository.getTotalRepsCount()
    }
}