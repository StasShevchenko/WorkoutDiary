package com.example.workoutdiary.presentation.add_edit_training_block_screen

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.data.model.entities.Muscle
import com.example.workoutdiary.data.model.entities.TrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import com.example.workoutdiary.domain.use_case.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
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
    val parameterizedSets: StateFlow<MutableList<ParameterizedSet>> =
        MutableStateFlow(MutableList(1, init = { ParameterizedSet(setOrder = 1) }))

    private val currentTrainingId: Int

    private val currentTrainingBlockId: Int? = null

    private var setOrder: Int? = null

    private val _setCounter: MutableStateFlow<Int> = MutableStateFlow(1)
    val setCounter: StateFlow<Int> = _setCounter

    private val _muscles: MutableStateFlow<List<Muscle>> = MutableStateFlow(listOf())
    val muscles: StateFlow<List<Muscle>> = _muscles

    private val _exercises: MutableStateFlow<List<Exercise>> = MutableStateFlow(listOf())
    val exercise: StateFlow<List<Exercise>> = _exercises

    private val _currentExercise: MutableStateFlow<Exercise?> = MutableStateFlow(null)
    val currentExercise: StateFlow<Exercise?> = _currentExercise

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    init {
        currentTrainingId = state.get<Int>("trainingId")!!
        if (state.get<Int>("trainingBlockId") == -1) {
            setOrder = state.get("setOrder")!!
        }
        viewModelScope.launch {
            getMuscles().collect { muscles ->
                _muscles.value = muscles
            }
        }
    }

    fun onEvent(event: AddEditTrainingBlockScreenEvent) {
        when (event) {
            is AddEditTrainingBlockScreenEvent.SetNumberDecreased -> {
                if (_setCounter.value > 1) {
                    _setCounter.value--
                    parameterizedSets.value.removeLast()
                }
            }
            is AddEditTrainingBlockScreenEvent.SetNumberIncreased -> {
                if (_setCounter.value < 10) {
                    _setCounter.value++
                    parameterizedSets.value.add(ParameterizedSet(setOrder = setCounter.value))
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
            is AddEditTrainingBlockScreenEvent.RepsEntered -> {
                parameterizedSets.value[event.index] = parameterizedSets.value[event.index].copy(
                    repeats = if (event.value.isNotEmpty() && event.value.isDigitsOnly()) event.value.toInt() else null
                )
            }
            is AddEditTrainingBlockScreenEvent.WeightEntered -> {
                parameterizedSets.value[event.index] = parameterizedSets.value[event.index].copy(
                    weight = if (event.value.isNotEmpty() && event.value.isDigitsOnly()) event.value.toInt() else null
                )
            }
            is AddEditTrainingBlockScreenEvent.SaveChosen -> {
                viewModelScope.launch {
                    if (currentTrainingBlockId == null) {
                        insertTrainingBlock(
                            TrainingBlock(0, setOrder!!, currentTrainingId, currentExercise.value!!.exerciseId),
                            parameterizedSets.value
                        )
                        _eventFlow.emit(UiEvent.SavePressed)
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        object SavePressed : UiEvent()
    }
}