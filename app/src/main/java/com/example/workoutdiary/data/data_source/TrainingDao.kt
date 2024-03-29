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

    @Query("SELECT * FROM TRAINING WHERE trainingId = :trainingId")
    suspend fun getTrainingById(trainingId: Int): Training

    @Query("DELETE FROM Training WHERE trainingId = :trainingId")
    suspend fun deleteTraining(trainingId: Int)

    @Query("SELECT * FROM Training WHERE isFavourite GROUP BY trainingName HAVING MAX(trainingDate)")
    fun getAllFavouritesTrainings(): Flow<List<Training>>

    @Query("UPDATE Training SET isFavourite = 0 WHERE trainingName = :trainingName")
    suspend fun clearFavouriteTrainingsByTrainingName(trainingName: String)

    @Query("UPDATE TRAINING SET isFavourite = 1 WHERE trainingName = :trainingName")
    suspend fun restoreFavouriteTrainingsByTrainingName(trainingName: String)
}