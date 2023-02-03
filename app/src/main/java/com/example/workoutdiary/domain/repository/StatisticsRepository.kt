package com.example.workoutdiary.domain.repository

import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {

    fun getTotalTrainingCount(): Flow<Int>

    fun getTotalSetsCount(): Flow<Int>

    fun getTotalRepsCount(): Flow<Int?>

    fun getTotalWeightCount(): Flow<Int?>
}