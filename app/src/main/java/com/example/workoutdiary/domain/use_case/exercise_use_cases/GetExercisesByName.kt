package com.example.workoutdiary.domain.use_case.exercise_use_cases

import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetExercisesByName(
    private val repository: ExerciseRepository
) {
    operator fun invoke(exerciseName: String): Flow<List<Exercise>> {
        return repository.getAllExercises().map { exercises ->
            if(exerciseName.isEmpty()){
                exercises
            } else{
                exercises.filter { exercise ->
                    exercise.exerciseName.contains(exerciseName, ignoreCase = true)
                }
            }
        }
    }
}