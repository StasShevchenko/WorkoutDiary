package com.example.workoutdiary.data.model.relation_entities

import com.example.workoutdiary.utils.ExerciseType


/*
I use this class to retrieve data
from both of TrainingBlock and Exercise
entities at the same time
 */
data class ExerciseTrainingBlock(
    val trainingBlockId: Int,
    val trainingBlockOrder: Int,
    val exerciseName: String,
    val exerciseType: ExerciseType,
    val exerciseId: Int
)
