package com.example.workoutdiary.data.data_source

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.data.model.relation_entities.ExerciseTrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import kotlinx.coroutines.flow.Flow


@Dao
interface TrainingDao {

    @Query("SELECT * FROM Training")
    fun getTrainings(): Flow<List<Training>>

    @Query("SELECT trainingBlockId, trainingBlockOrder, exerciseName," +
            "exerciseType, setOrder, repeats, weight, time, distance " +
            "FROM TrainingBlock JOIN Exercise USING(exerciseId)" +
            "JOIN" +
            "(SELECT trainingBlockId, setOrder, repeats, weight, time, distance FROM " +
            "OrderedSet JOIN TrainingParameters USING(trainingParametersId))" +
            "USING (trainingBlockId) WHERE trainingId = :trainingId")
      fun getTrainingDetailsByTrainingId(trainingId: Int): Flow<Map<ExerciseTrainingBlock, List<ParameterizedSet>>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTraining(training: Training): Long

    @Delete
    suspend fun deleteTraining(training: Training)
}