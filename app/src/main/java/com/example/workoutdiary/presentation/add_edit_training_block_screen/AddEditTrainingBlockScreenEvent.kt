package com.example.workoutdiary.presentation.add_edit_training_block_screen

sealed class AddEditTrainingBlockScreenEvent {
    object SetNumberIncreased : AddEditTrainingBlockScreenEvent()
    object SetNumberDecreased : AddEditTrainingBlockScreenEvent()
}
