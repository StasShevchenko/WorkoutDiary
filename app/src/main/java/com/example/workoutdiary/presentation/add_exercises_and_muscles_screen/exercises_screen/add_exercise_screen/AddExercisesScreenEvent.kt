package com.example.workoutdiary.presentation.add_exercises_and_muscles_screen.exercises_screen.add_exercise_screen

import com.example.workoutdiary.data.model.entities.Muscle
import com.example.workoutdiary.utils.ExerciseType

sealed class AddExercisesScreenEvent{
    data class ExerciseNameEntered(val exerciseName: String) : AddExercisesScreenEvent()
    data class MuscleSelected(val muscle: Muscle) : AddExercisesScreenEvent()
    data class ExerciseDescriptionEntered(val description: String) : AddExercisesScreenEvent()
    data class ExerciseTypeSelected(val exerciseType: ExerciseType) : AddExercisesScreenEvent()
    object ExerciseAdded : AddExercisesScreenEvent()
    object UpdateConfirmed : AddExercisesScreenEvent()
}