package com.example.workoutdiary.data.data_source

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StatisticsDao {

    @Query("SELECT COUNT(*) FROM training")
    fun getTotalTrainingsCount(): Flow<Int>

    //There we use Int because we always have orderedSets table
    @Query("SELECT COUNT(*) FROM orderedSet")
    fun getTotalSetsCount(): Flow<Int>

    //If join is empty (no result table) function will return null
    @Query("SELECT SUM(repeats) FROM trainingParameters JOIN orderedSet USING(trainingParametersId)")
    fun getTotalRepsCount(): Flow<Int?>

    @Query("SELECT SUM(weight * repeats) FROM TrainingParameters JOIN OrderedSet USING(trainingParametersId)")
    fun getTotalWeightCount(): Flow<Int?>

}