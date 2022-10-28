package com.example.workoutdiary.utils

enum class ExerciseType (val value: String) {
    REPS("REPS"),
    WEIGHT_AND_REPS ("WEIGHT AND REPS"),
    TIME("TIME"),
    DISTANCE("DISTANCE");

    companion object{
        fun getTypeByValue(value: String): ExerciseType {
            return when(value){
                "REPS" -> REPS
                "WEIGHT AND REPS"-> WEIGHT_AND_REPS
                "TIME" -> TIME
                "DISTANCE" -> DISTANCE
                else -> REPS
            }
        }
    }

}