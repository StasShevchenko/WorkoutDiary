package com.example.workoutdiary.data.data_source

import androidx.room.*
import com.example.workoutdiary.data.model.entities.Muscle
import kotlinx.coroutines.flow.Flow


@Dao
interface MuscleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMuscle(muscle: Muscle)

    @Query("SELECT * FROM Muscle ORDER BY muscleName")
    fun getAllMuscles(): Flow<List<Muscle>>

    @Delete
    suspend fun deleteMuscle(muscle: Muscle)
}