package com.example.workoutdiary.data.repository

import com.example.workoutdiary.data.data_source.MuscleDao
import com.example.workoutdiary.data.model.entities.Muscle
import com.example.workoutdiary.domain.repository.MuscleRepository
import kotlinx.coroutines.flow.Flow

class MuscleRepositoryImpl(
    private val dao: MuscleDao
) : MuscleRepository {
    override fun getAllMuscles(): Flow<Muscle> {
        return dao.getAllMuscles()
    }

    override suspend fun insertMuscle(muscle: Muscle) {
        dao.insertMuscle(muscle)
    }

    override suspend fun deleteMuscle(muscle: Muscle) {
        dao.deleteMuscle(muscle)
    }
}