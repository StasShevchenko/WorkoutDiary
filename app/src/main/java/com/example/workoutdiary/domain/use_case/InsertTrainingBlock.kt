package com.example.workoutdiary.domain.use_case

import androidx.room.Index
import com.example.workoutdiary.data.model.entities.OrderedSet
import com.example.workoutdiary.data.model.entities.TrainingBlock
import com.example.workoutdiary.data.model.entities.TrainingParameters
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import com.example.workoutdiary.domain.repository.TrainingDetailsRepository

class InsertTrainingBlock(
    private val repository: TrainingDetailsRepository
) {
    suspend operator fun invoke(
        trainingBlock: TrainingBlock,
        parameterizedSets: List<ParameterizedSet>
    ) {
        val trainingBlockId = repository.insertTrainingBlock(trainingBlock)
        repository.deleteOrderedSets(trainingBlock.trainingBlockId)
        val resOrderedSets: MutableList<OrderedSet> =
            arrayListOf()
        parameterizedSets.forEach {
            val parameters: TrainingParameters? = repository.getTrainingParametersByParams(
                it.repeats,
                it.weight,
                it.time,
                it.distance
            )
            if (parameters == null) {
                val newParametersId = repository.insertTrainingParameters(
                    TrainingParameters(
                        repeats = it.repeats,
                        weight = it.weight,
                        time = it.time,
                        distance = it.distance
                    )
                )
                resOrderedSets.add(
                    OrderedSet(
                        setOrder = it.setOrder,
                        trainingParametersId = newParametersId.toInt(),
                        trainingBlockId = trainingBlockId.toInt()
                    )
                )
            } else{
                resOrderedSets.add(
                    OrderedSet(
                        setOrder = it.setOrder,
                        trainingParametersId = parameters.trainingParametersId.toInt(),
                        trainingBlockId = trainingBlockId.toInt()
                    )
                )
            }
        }
        repository.insertOrderedSets(resOrderedSets)
    }
}