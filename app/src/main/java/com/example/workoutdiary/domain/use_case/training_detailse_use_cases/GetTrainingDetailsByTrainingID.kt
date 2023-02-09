package com.example.workoutdiary.domain.use_case.training_detailse_use_cases

import com.example.workoutdiary.data.model.relation_entities.ExerciseTrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import com.example.workoutdiary.domain.repository.TrainingRepository
import kotlinx.coroutines.flow.Flow

class GetTrainingDetailsByTrainingID(
    private val trainingRepository: TrainingRepository
) {
    operator fun invoke(trainingId: Int): Flow<Map<ExerciseTrainingBlock, List<ParameterizedSet>>> {
        return  trainingRepository.getTrainingDetailsByTrainingId(trainingId)
    }
}