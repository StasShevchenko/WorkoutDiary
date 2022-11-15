package com.example.workoutdiary.data.data_source

import androidx.room.*
import com.example.workoutdiary.data.model.entities.Exercise
import kotlinx.coroutines.flow.Flow


@Dao
interface ExerciseDao {

    @Query("SELECT * FROM Exercise WHERE muscleId = :muscleId")
    suspend fun getExercisesByMuscleId(muscleId: Int): List<Exercise>

    @Query("SELECT * FROM Exercise ORDER BY exerciseName")
    fun getAllExercises(): Flow<List<Exercise>>

    @Query("SELECT * FROM Exercise WHERE exerciseId = :exerciseId")
    suspend fun getExerciseById(exerciseId: Int): Exercise

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise)

    @Query("SELECT * FROM Exercise WHERE exerciseName = :exerciseName")
    suspend fun getExerciseByName(exerciseName: String): Exercise?
}