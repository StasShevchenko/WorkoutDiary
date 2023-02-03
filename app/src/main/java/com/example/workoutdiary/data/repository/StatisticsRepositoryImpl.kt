package com.example.workoutdiary.data.repository

import com.example.workoutdiary.data.data_source.StatisticsDao
import com.example.workoutdiary.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow

class StatisticsRepositoryImpl(
    private val dao: StatisticsDao
) : StatisticsRepository {

    override fun getTotalTrainingCount(): Flow<Int> {
        return dao.getTotalTrainingsCount()
    }

    override fun getTotalSetsCount(): Flow<Int> {
        return dao.getTotalSetsCount()
    }

    override fun getTotalRepsCount(): Flow<Int?> {
        return dao.getTotalRepsCount()
    }

    override fun getTotalWeightCount(): Flow<Int?> {
        return dao.getTotalWeightCount()
    }
}