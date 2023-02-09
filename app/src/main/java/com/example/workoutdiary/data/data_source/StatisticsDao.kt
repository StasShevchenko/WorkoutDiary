package com.example.workoutdiary.data.data_source

import androidx.room.*
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

    @MapInfo(keyColumn = "trainingDate", valueColumn = "max")
    @Query(
        "SELECT trainingDate, MAX(repeats) AS max FROM Training JOIN TrainingBlock USING(trainingId) JOIN Exercise USING (exerciseId)" +
                "JOIN OrderedSet USING(trainingBlockId) JOIN TrainingParameters USING(trainingParametersId) WHERE exerciseName =:exerciseName  GROUP BY trainingDate ORDER BY trainingDate"
    )
    fun getExerciseRepsStatisticsInfo(exerciseName: String): Flow<Map<LocalDate, Int>>

    @MapInfo(keyColumn = "trainingDate", valueColumn = "max")
    @Query(
        "SELECT trainingDate, MAX(weight) AS max FROM Training JOIN TrainingBlock USING(trainingId) JOIN Exercise USING (exerciseId)" +
                "JOIN OrderedSet USING(trainingBlockId) JOIN TrainingParameters USING(trainingParametersId) WHERE exerciseName =:exerciseName GROUP BY trainingDate ORDER BY trainingDate"
    )
    fun getExerciseWeightStatisticsInfo(exerciseName: String): Flow<Map<LocalDate, Int>>
    @MapInfo(keyColumn = "trainingDate", valueColumn = "max")
    @Query(
        "SELECT trainingDate, MAX(time) AS max FROM Training JOIN TrainingBlock USING(trainingId) JOIN Exercise USING (exerciseId)" +
                "JOIN OrderedSet USING(trainingBlockId) JOIN TrainingParameters USING(trainingParametersId) WHERE exerciseName =:exerciseName GROUP BY trainingDate ORDER BY trainingDate"
    )
    fun getExerciseTimeStatisticsInfo(exerciseName: String): Flow<Map<LocalDate, Int>>

    @MapInfo(keyColumn = "trainingDate", valueColumn = "max")
    @Query(
        "SELECT trainingDate, MAX(distance) as max FROM Training JOIN TrainingBlock USING(trainingId) JOIN Exercise USING (exerciseId)" +
                "JOIN OrderedSet USING(trainingBlockId) JOIN TrainingParameters USING(trainingParametersId) WHERE exerciseName =:exerciseName GROUP BY trainingDate ORDER BY trainingDate"
    )
    fun getExerciseDistanceStatisticsInfo(exerciseName: String): Flow<Map<LocalDate, Int>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExerciseStatisticsParameters(statisticsInfo: ExerciseStatisticsParameters)

    @Query("SELECT * FROM ExerciseStatisticsParameters")
    fun getExerciseStatisticsParameters(): Flow<ExerciseStatisticsParameters?>

    @Delete
    suspend fun deleteExerciseStatisticsParameters(statisticsParameters: ExerciseStatisticsParameters)

}