package com.example.workoutdiary.presentation.add_edit_training_screen

sealed class AddEditTrainingScreenEvent{
    data class NameEntered(val text: String) : AddEditTrainingScreenEvent()
    data class TrainingBlockSwapped(val fromPosition: Int, val toPosition: Int): AddEditTrainingScreenEvent()
    object SwapFinished: AddEditTrainingScreenEvent()
    object OnBackPressed : AddEditTrainingScreenEvent()
    object DeletePressed : AddEditTrainingScreenEvent()
}
