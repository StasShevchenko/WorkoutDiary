package com.example.workoutdiary.presentation.exercise_details_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.domain.use_case.exercise_use_cases.GetExercise
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseDetailsViewModel @Inject constructor(
    private val getExercise: GetExercise,
    private val state: SavedStateHandle,
) : ViewModel() {
    private val _currentExercise: MutableStateFlow<Exercise?> = MutableStateFlow(null)
    val currentExercise: StateFlow<Exercise?> = _currentExercise

    init {
        val exerciseId = state.get<Int>("exerciseId")
        viewModelScope.launch {
            val exercise = getExercise(exerciseId!!)
            _currentExercise.value = exercise
        }
    }
}