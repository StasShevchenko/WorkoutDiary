package com.example.workoutdiary.data.repository

import com.example.workoutdiary.data.data_source.TrainingDao
import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.data.model.relation_entities.ExerciseTrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import com.example.workoutdiary.domain.repository.TrainingRepository
import kotlinx.coroutines.flow.Flow

class TrainingRepositoryImpl(private val trainingDao: TrainingDao) : TrainingRepository {
    override fun getAllTrainings(): Flow<List<Training>> {
        return trainingDao.getAllTrainings()
    }

    override fun getTrainingDetailsByTrainingId(trainingId: Int): Flow<Map<ExerciseTrainingBlock, List<ParameterizedSet>>> {
        return trainingDao.getTrainingDetailsByTrainingId(trainingId)
    }

    override suspend fun insertTraining(training: Training): Long {
        return trainingDao.insertTraining(training)
    }

    override suspend fun deleteTraining(trainingId: Int) {
        trainingDao.deleteTraining(trainingId)
    }
}