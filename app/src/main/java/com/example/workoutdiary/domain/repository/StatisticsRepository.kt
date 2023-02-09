package com.example.workoutdiary.domain.repository

import com.example.workoutdiary.data.model.entities.ExerciseStatisticsParameters
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface StatisticsRepository {

    fun getTotalTrainingCount(): Flow<Int>

    fun getTotalSetsCount(): Flow<Int>

    fun getTotalRepsCount(): Flow<Int?>

    fun getTotalWeightCount(): Flow<Int?>

    fun getExerciseStatisticsInfo(exerciseName: String, statisticsParameter: String): Flow<Map<LocalDate, Int>>

    suspend fun insertExerciseStatisticsParameters(exerciseStatisticsParameters: ExerciseStatisticsParameters)

    fun getExerciseStatisticsParameters(): Flow<ExerciseStatisticsParameters?>

    suspend fun deleteExerciseStatisticsParameters(statisticsParameters: ExerciseStatisticsParameters)

}