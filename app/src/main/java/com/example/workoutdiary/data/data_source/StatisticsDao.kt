package com.example.workoutdiary.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.workoutdiary.data.model.entities.ExerciseStatisticsParameters
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

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

    @Query(
        "SELECT trainingDate, MAX(repeats) FROM Training JOIN TrainingBlock USING(trainingId) JOIN Exercise USING (exerciseId)" +
                "JOIN OrderedSet USING(trainingBlockId) JOIN TrainingParameters USING(trainingParametersId) WHERE exerciseName =:exerciseName ORDER BY trainingDate"
    )
    fun getExerciseRepsStatisticsInfo(exerciseName: String): Flow<Map<LocalDate, List<Int>>>

    @Query(
        "SELECT trainingDate, MAX(weight) FROM Training JOIN TrainingBlock USING(trainingId) JOIN Exercise USING (exerciseId)" +
                "JOIN OrderedSet USING(trainingBlockId) JOIN TrainingParameters USING(trainingParametersId) WHERE exerciseName =:exerciseName ORDER BY trainingDate"
    )
    fun getExerciseWeightStatisticsInfo(exerciseName: String): Flow<Map<LocalDate, List<Int>>>

    @Query(
        "SELECT trainingDate, MAX(time) FROM Training JOIN TrainingBlock USING(trainingId) JOIN Exercise USING (exerciseId)" +
                "JOIN OrderedSet USING(trainingBlockId) JOIN TrainingParameters USING(trainingParametersId) WHERE exerciseName =:exerciseName ORDER BY trainingDate"
    )
    fun getExerciseTimeStatisticsInfo(exerciseName: String): Flow<Map<LocalDate, List<Int>>>

    @Query(
        "SELECT trainingDate, MAX(distance) FROM Training JOIN TrainingBlock USING(trainingId) JOIN Exercise USING (exerciseId)" +
                "JOIN OrderedSet USING(trainingBlockId) JOIN TrainingParameters USING(trainingParametersId) WHERE exerciseName =:exerciseName ORDER BY trainingDate"
    )
    fun getExerciseDistanceStatisticsInfo(exerciseName: String): Flow<Map<LocalDate, List<Int>>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseStatisticsParameters(statisticsInfo: ExerciseStatisticsParameters)

    @Query("SELECT * FROM ExerciseStatisticsParameters")
    suspend fun getExerciseStatisticsParameters(): ExerciseStatisticsParameters

}