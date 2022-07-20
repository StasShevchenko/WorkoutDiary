package com.example.workoutdiary.presentation.add_edit_training_screen

sealed class AddEditTrainingScreenEvent{
    data class NameEntered(val text: String) : AddEditTrainingScreenEvent()
    object OnBackPressed : AddEditTrainingScreenEvent()
    object DeletePressed : AddEditTrainingScreenEvent()
}
