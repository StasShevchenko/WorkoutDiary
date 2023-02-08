package com.example.workoutdiary.data.repository

import com.example.workoutdiary.data.data_source.StatisticsDao
import com.example.workoutdiary.data.model.entities.ExerciseStatisticsParameters
import com.example.workoutdiary.domain.repository.StatisticsRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

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

    override fun getExerciseStatisticsInfo(
        exerciseName: String,
        statisticsParameter: String
    ): Flow<Map<LocalDate, List<Int>>> {
        when (statisticsParameter) {
            "repeats" -> {
                return dao.getExerciseRepsStatisticsInfo(exerciseName)
            }
            "weight" ->{
                return dao.getExerciseWeightStatisticsInfo(exerciseName)
            }
            "time" ->{
                return dao.getExerciseTimeStatisticsInfo(exerciseName)
            }
            "distance" ->{
                return dao.getExerciseDistanceStatisticsInfo(exerciseName)
            }
            else ->{
                return dao.getExerciseRepsStatisticsInfo(exerciseName)
            }
        }
    }

    override suspend fun insertExerciseStatisticsParameters(exerciseStatisticsParameters: ExerciseStatisticsParameters) {
         dao.insertExerciseStatisticsParameters(exerciseStatisticsParameters)
    }

    override suspend fun getExerciseStatisticsParameters(): ExerciseStatisticsParameters {
        return dao.getExerciseStatisticsParameters()
    }
}