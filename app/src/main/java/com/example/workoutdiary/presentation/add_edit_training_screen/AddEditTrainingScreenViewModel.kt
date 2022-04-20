package com.example.workoutdiary.presentation.add_edit_training_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddEditTrainingScreenViewModel @Inject constructor(
    private val state: SavedStateHandle
) : ViewModel() {
    lateinit var date: LocalDate
        private set

    private var currentTrainingId = -1


    init {
        date = state.get<LocalDate>("trainingDate")!!
        currentTrainingId = state.get<Int>("trainingId")!!
    }
}