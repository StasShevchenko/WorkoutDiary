package com.example.workoutdiary.domain.use_case

import com.example.workoutdiary.data.model.relation_entities.ExerciseTrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import com.example.workoutdiary.domain.repository.TrainingDetailsRepository
import kotlinx.coroutines.flow.Flow

class GetTrainingBlockWithDetailsByTrainingBlockId(
    private val repository: TrainingDetailsRepository
) {
    operator fun invoke(trainingBlockId: Int): Flow<Map<ExerciseTrainingBlock, List<ParameterizedSet>>> {
       return repository.getTrainingBlockWithDetailsByTrainingBlockId(trainingBlockId)
    }
}