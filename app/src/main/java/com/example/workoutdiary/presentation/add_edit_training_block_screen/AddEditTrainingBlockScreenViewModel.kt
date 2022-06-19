package com.example.workoutdiary.presentation.add_edit_training_block_screen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.data.model.entities.Muscle
import com.example.workoutdiary.domain.use_case.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTrainingBlockScreenViewModel @Inject constructor(
    private val state: SavedStateHandle,
    private val trainingBlockDetails: GetTrainingBlockWithDetailsByTrainingBlockId,
    private val insertTrainingBlock: InsertTrainingBlock,
    private val getMuscles: GetMuscles,
    private val getExercisesByMuscleId: GetExercisesByMuscleId,
    private val deleteTrainingBlock: DeleteTrainingBlock
) : ViewModel() {

    private val _setCounter: MutableStateFlow<Int> = MutableStateFlow(1)
    val setCounter: StateFlow<Int> = _setCounter

    private val _muscles: MutableStateFlow<List<Muscle>> = MutableStateFlow(listOf())
    val muscles: StateFlow<List<Muscle>> = _muscles

    private val _exercises: MutableStateFlow<List<Exercise>> = MutableStateFlow(listOf())
    val exercise: StateFlow<List<Exercise>> = _exercises

    private val _currentExercise: MutableStateFlow<Exercise?> = MutableStateFlow(null)
    val currentExercise: StateFlow<Exercise?> = _currentExercise


    init {
        viewModelScope.launch {
            getMuscles().collect{ muscles ->
                _muscles.value = muscles
            }
        }
    }

    fun onEvent(event: AddEditTrainingBlockScreenEvent) {
        when (event) {
            is AddEditTrainingBlockScreenEvent.SetNumberDecreased -> {
                if (_setCounter.value > 1) {
                    _setCounter.value--
                }
            }
            is AddEditTrainingBlockScreenEvent.SetNumberIncreased ->  {
                if (_setCounter.value < 10) {
                    _setCounter.value++
                }
            }
            is AddEditTrainingBlockScreenEvent.MuscleSelected -> {
                viewModelScope.launch {
                    _exercises.value = getExercisesByMuscleId(event.muscle.muscleId)
                }
            }
            is AddEditTrainingBlockScreenEvent.ExerciseSelected -> {
                _currentExercise.value = event.exercise
            }
        }
    }
}