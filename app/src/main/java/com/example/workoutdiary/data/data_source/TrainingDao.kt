package com.example.workoutdiary.data.data_source

import androidx.room.*
import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.data.model.entities.TrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ExerciseTrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import kotlinx.coroutines.flow.Flow


@Dao
interface TrainingDao {

    @Query("DELETE FROM Training WHERE NOT EXISTS (SELECT * FROM TrainingBlock " +
            "WHERE Training.trainingId = TrainingBlock.trainingId)")
    suspend fun deleteEmptyTrainings()

    @Query("SELECT * FROM Training")
    fun getAllTrainings(): Flow<List<Training>>

    @Query("SELECT trainingId, trainingBlockId, trainingBlockOrder, exerciseName," +
            "exerciseType, exerciseId, setOrder, repeats, weight, time, distance " +
            "FROM TrainingBlock JOIN Exercise USING(exerciseId)" +
            "JOIN" +
            "(SELECT trainingBlockId, setOrder, repeats, weight, time, distance FROM " +
            "OrderedSet JOIN TrainingParameters USING(trainingParametersId))" +
            "USING (trainingBlockId) WHERE trainingId = :trainingId ORDER BY trainingBlockOrder ASC")
      fun getTrainingDetailsByTrainingId(trainingId: Int): Flow<Map<ExerciseTrainingBlock, List<ParameterizedSet>>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTrainingBlocks(trainingBlocks: List<TrainingBlock>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTraining(training: Training): Long

    @Query("DELETE FROM Training WHERE trainingId = :trainingId")
    suspend fun deleteTraining(trainingId: Int)

}