package com.example.workoutdiary.data.repository

import com.example.workoutdiary.data.data_source.ExerciseDao
import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow

class ExerciseRepositoryImpl(
    private val dao: ExerciseDao
): ExerciseRepository {
    override fun getAllExercises(): Flow<List<Exercise>> {
        return dao.getAllExercises()
    }

    override suspend fun getExercisesByMuscleId(muscleId: Int): List<Exercise> {
        return dao.getExercisesByMuscleId(muscleId)
    }

    override suspend fun deleteExercise(exercise: Exercise) {
        dao.deleteExercise(exercise)
    }

    override suspend fun insertExercise(exercise: Exercise) {
        dao.insertExercise(exercise)
    }

    override suspend fun getExerciseById(exerciseId: Int): Exercise {
        return dao.getExerciseById(exerciseId)
    }
}