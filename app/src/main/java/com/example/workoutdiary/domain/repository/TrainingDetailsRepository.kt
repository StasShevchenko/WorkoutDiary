package com.example.workoutdiary.domain.repository

import com.example.workoutdiary.data.model.entities.OrderedSet
import com.example.workoutdiary.data.model.entities.TrainingBlock
import com.example.workoutdiary.data.model.entities.TrainingParameters
import com.example.workoutdiary.data.model.relation_entities.ExerciseTrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import kotlinx.coroutines.flow.Flow

interface TrainingDetailsRepository {

    suspend fun deleteTrainingBlock(trainingBlockId: Int)

    suspend fun deleteOrderedSets(trainingBlockId: Int)

    suspend fun insertTrainingBlock(trainingBlock: TrainingBlock): Long

    suspend fun insertOrderedSets(orderedSets: List<OrderedSet>)

    suspend fun getTrainingParametersByParams(
        repeats: Int? = null,
        weight: Int? = null,
        time: Int? = null,
        distance: Int? = null
    ): TrainingParameters?

    suspend fun insertTrainingParameters(trainingParameters: TrainingParameters): Long

    fun getTrainingBlockWithDetailsByTrainingBlockId(trainingBlockId: Int): Flow<Map<ExerciseTrainingBlock, List<ParameterizedSet>>>
}