package com.example.workoutdiary.data.data_source

import androidx.room.*
import com.example.workoutdiary.data.model.entities.TrainingBlock
import com.example.workoutdiary.data.model.entities.TrainingParameters
import com.example.workoutdiary.data.model.relation_entities.ExerciseTrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import kotlinx.coroutines.flow.Flow


@Dao
interface TrainingDetailsDao {

    @Query("DELETE FROM TrainingBlock WHERE trainingBlockId = :trainingBlockId")
    suspend fun deleteTrainingBlock(trainingBlockId: Int)

    @Query("DELETE FROM OrderedSet WHERE trainingBlockId = :trainingBlockId")
    suspend fun deleteOrderedSets(trainingBlockId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrainingBlock(trainingBlock: TrainingBlock): Long

    @Query("SELECT * FROM TrainingParameters WHERE " +
            "repeats = :repeats AND weight = :weight AND time = :time AND distance = :distance")
    suspend fun getTrainingParametersByParams(
       repeats: Int? = null,
       weight: Int? = null,
       time: Int? = null,
       distance: Int? = null
    ): TrainingParameters

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrainingParameters(trainingParameters: TrainingParameters)

    @Query("SELECT trainingBlockId, trainingBlockOrder, exerciseName," +
            "exerciseType, setOrder, repeats, weight, time, distance " +
            "FROM TrainingBlock JOIN Exercise USING(exerciseId)" +
            "JOIN" +
            "(SELECT trainingBlockId, setOrder, repeats, weight, time, distance FROM " +
            "OrderedSet JOIN TrainingParameters USING(trainingParametersId))" +
            "USING (trainingBlockId) WHERE trainingBlockId = :trainingBlockId")
    fun getTrainingBlockWithDetailsByTrainingBlockId(trainingBlockId: Int): Flow<Map<ExerciseTrainingBlock, List<ParameterizedSet>>>

}