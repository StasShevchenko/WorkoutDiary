package com.example.workoutdiary.domain.use_case.exercise_use_cases

import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.data.model.entities.TrainingBlock
import com.example.workoutdiary.domain.repository.ExerciseRepository
import com.example.workoutdiary.domain.repository.TrainingDetailsRepository

class DeleteExercise(
    private val exerciseRepository: ExerciseRepository,
    private val trainingDetailsRepository: TrainingDetailsRepository
) {
    suspend operator fun invoke(exercise: Exercise) {
        val trainingBlockList: List<TrainingBlock> = trainingDetailsRepository.getTrainingBlocksByExerciseId(exercise.exerciseId)
        trainingBlockList.forEach{trainingBlock ->  
            trainingDetailsRepository.deleteOrderedSets(trainingBlock.trainingBlockId)
            trainingDetailsRepository.deleteTrainingBlock(trainingBlock.trainingBlockId)
        }
        exerciseRepository.deleteExercise(exercise)
    }
}