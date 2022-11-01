package com.example.workoutdiary.domain.repository

import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.data.model.entities.TrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ExerciseTrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import kotlinx.coroutines.flow.Flow

interface TrainingRepository {

    fun getAllTrainings(): Flow<List<Training>>

    fun getTrainingDetailsByTrainingId(trainingId: Int): Flow<Map<ExerciseTrainingBlock, List<ParameterizedSet>>>

    suspend fun insertTraining(training: Training): Long

    suspend fun deleteTraining(trainingId: Int)

    suspend fun updateTrainingBlocks(trainingBlocks: List<TrainingBlock>)
}