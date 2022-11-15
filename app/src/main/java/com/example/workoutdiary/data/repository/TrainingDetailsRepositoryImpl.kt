package com.example.workoutdiary.data.repository

import com.example.workoutdiary.data.data_source.TrainingDetailsDao
import com.example.workoutdiary.data.model.entities.OrderedSet
import com.example.workoutdiary.data.model.entities.TrainingBlock
import com.example.workoutdiary.data.model.entities.TrainingParameters
import com.example.workoutdiary.data.model.relation_entities.ExerciseTrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import com.example.workoutdiary.domain.repository.TrainingDetailsRepository
import kotlinx.coroutines.flow.Flow

class TrainingDetailsRepositoryImpl(
    private val dao: TrainingDetailsDao
) : TrainingDetailsRepository {
    override suspend fun deleteTrainingBlock(trainingBlockId: Int) {
        dao.deleteTrainingBlock(trainingBlockId)
    }

    override suspend fun deleteOrderedSets(trainingBlockId: Int) {
        dao.deleteOrderedSets(trainingBlockId)
    }

    override suspend fun insertTrainingBlock(trainingBlock: TrainingBlock): Long {
       return dao.insertTrainingBlock(trainingBlock)
    }

    override suspend fun insertOrderedSets(orderedSets: List<OrderedSet>) {
      dao.insertOrderedSets(orderedSets)
    }

    override suspend fun getTrainingParametersByParams(
        repeats: Int?,
        weight: Int?,
        time: Int?,
        distance: Int?
    ): TrainingParameters? {
        return dao.getTrainingParametersByParams(
            repeats,
            weight,
            time,
            distance
        )
    }

    override suspend fun insertTrainingParameters(trainingParameters: TrainingParameters): Long {
        return dao.insertTrainingParameters(trainingParameters)
    }

    override suspend fun deleteTrainingBlocksByTrainingId(trainingId: Int) {
         dao.deleteTrainingBlocksByTrainingId(trainingId)
    }

    override suspend fun deleteOrderedSetsByTrainingId(trainingId: Int) {
        dao.deleteOrderedSetsByTrainingId(trainingId)
    }

    override fun getTrainingBlockWithDetailsByTrainingBlockId(trainingBlockId: Int): Flow<Map<ExerciseTrainingBlock, List<ParameterizedSet>>> {
       return dao.getTrainingBlockWithDetailsByTrainingBlockId(trainingBlockId)
    }

    override suspend fun getTrainingBlocksByExerciseId(exerciseId: Int): List<TrainingBlock> {
        return dao.getTrainingBlocksByExerciseId(exerciseId)
    }
}