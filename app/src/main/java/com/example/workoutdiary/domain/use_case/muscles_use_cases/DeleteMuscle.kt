package com.example.workoutdiary.domain.use_case.muscles_use_cases

import com.example.workoutdiary.data.model.entities.Muscle
import com.example.workoutdiary.domain.repository.ExerciseRepository
import com.example.workoutdiary.domain.repository.MuscleRepository
import com.example.workoutdiary.domain.repository.TrainingDetailsRepository

class DeleteMuscle(
    private val muscleRepository: MuscleRepository,
    private val exerciseRepository: ExerciseRepository,
    private val trainingDetailsRepository: TrainingDetailsRepository
) {
    suspend operator fun invoke(muscle: Muscle){
        val exercisesList = exerciseRepository.getExercisesByMuscleId(muscleId = muscle.muscleId)
        exercisesList.forEach{ exercise ->
            val trainingBlockList = trainingDetailsRepository.getTrainingBlocksByExerciseId(exercise.exerciseId)
            trainingBlockList.forEach { trainingBlock ->
                trainingDetailsRepository.deleteOrderedSets(trainingBlock.trainingBlockId)
                trainingDetailsRepository.deleteTrainingBlock(trainingBlock.trainingBlockId)
            }
            exerciseRepository.deleteExercise(exercise)
        }
        muscleRepository.deleteMuscle(muscle)
    }
}