package com.example.workoutdiary.presentation.add_exercises_and_muscles_screen.exercises_screen.add_exercise_screen

import com.example.workoutdiary.utils.ExerciseType

class ExerciseTypeMapper(
    private val valuesArray: Array<String>
) {

    fun getNameByValue(value: ExerciseType): String {
        return when (value) {
            ExerciseType.REPS -> {
                valuesArray[0]
            }
            ExerciseType.WEIGHT_AND_REPS -> {
                valuesArray[1]
            }
            ExerciseType.TIME -> {
                valuesArray[2]
            }
            ExerciseType.DISTANCE -> {
                valuesArray[3]
            }
        }
    }

    fun getValueByPosition(position: Int): ExerciseType{
        return when(position){
            0 ->{
              ExerciseType.REPS
            }
            1 ->{
                ExerciseType.WEIGHT_AND_REPS
            }
            2 ->{
                ExerciseType.TIME
            }
            3 ->{
                ExerciseType.DISTANCE
            }
            else -> {ExerciseType.REPS}
        }
    }
}