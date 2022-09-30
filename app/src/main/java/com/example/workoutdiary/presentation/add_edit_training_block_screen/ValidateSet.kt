package com.example.workoutdiary.presentation.add_edit_training_block_screen

import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet

data class ValidateSet(
    val setData: ParameterizedSet,
    val repsError: Boolean = false,
    val weightError: Boolean = false,
    val distanceError: Boolean = false,
    val timeError: Boolean = false
)
