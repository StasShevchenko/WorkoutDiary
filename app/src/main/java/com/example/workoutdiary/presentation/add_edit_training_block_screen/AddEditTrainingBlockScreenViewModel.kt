package com.example.workoutdiary.presentation.add_edit_training_block_screen

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.data.model.entities.Muscle
import com.example.workoutdiary.data.model.entities.TrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import com.example.workoutdiary.domain.use_case.*
import com.example.workoutdiary.utils.ExerciseType
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
    private val deleteTrainingBlock: DeleteTrainingBlock,
    private val getExercise: GetExercise,
    private val getMuscle: GetMuscle
) : ViewModel() {

    private val currentTrainingId: Int = state.get<Int>("trainingId")!!

    var currentTrainingBlockId: Int
        private set

    var isDataLoadingFinished = false
    private set

    private val _validateSets: MutableStateFlow<List<ValidateSet>> =
        MutableStateFlow(listOf())
    val validateSets: StateFlow<List<ValidateSet>> = _validateSets

    private var setOrder: Int? = null

    private val _muscles: MutableStateFlow<List<Muscle>> = MutableStateFlow(listOf())
    val muscles: StateFlow<List<Muscle>> = _muscles

    private val _exercises: MutableStateFlow<List<Exercise>> = MutableStateFlow(listOf())
    val exercise: StateFlow<List<Exercise>> = _exercises

    private val _currentExercise: MutableStateFlow<Exercise?> = MutableStateFlow(null)
    val currentExercise: StateFlow<Exercise?> = _currentExercise

    private val _currentMuscle: MutableStateFlow<Muscle?> = MutableStateFlow(null)
    val currentMuscle: StateFlow<Muscle?> = _currentMuscle

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    init {
        setOrder = state.get("setOrder")!!
        if (state.get<Int>("trainingBlockId") != -1) {
            currentTrainingBlockId = state.get<Int>("trainingBlockId")!!
            viewModelScope.launch {
                val exerciseId = state.get<Int>("exerciseId")
                _currentExercise.value = getExercise(exerciseId!!)
                _currentMuscle.value = getMuscle(_currentExercise.value!!.muscleId)
                _exercises.value = getExercisesByMuscleId(_currentMuscle.value!!.muscleId)
                trainingBlockDetails(currentTrainingBlockId).collectLatest { trainingBlockDetails ->
                    getMuscles().collect { muscles ->
                        isDataLoadingFinished = true
                        _muscles.value = muscles
                        _validateSets.value =
                            (trainingBlockDetails.values.toList()[0] as MutableList<ParameterizedSet>).map { parameterizedSet ->
                                ValidateSet(parameterizedSet)
                            }
                    }
                }
            }
        } else {
            currentTrainingBlockId = 0
            isDataLoadingFinished = true
            viewModelScope.launch {
                getMuscles().collect { muscles ->
                    _muscles.value = muscles
                }
            }
        }
    }

    fun onEvent(event: AddEditTrainingBlockScreenEvent) {
        when (event) {
            is AddEditTrainingBlockScreenEvent.SetNumberDecreased -> {
                if (_validateSets.value.isNotEmpty()) {
                    val newList = _validateSets.value.toMutableList()
                    newList.removeLast()
                    _validateSets.value = newList
                }
            }
            is AddEditTrainingBlockScreenEvent.SetNumberIncreased -> {
                if (_validateSets.value.size < 10) {
                    val newList = _validateSets.value.toMutableList()
                    newList.add(ValidateSet(ParameterizedSet(setOrder = _validateSets.value.size + 1)))
                    _validateSets.value = newList
                }
            }
            is AddEditTrainingBlockScreenEvent.MuscleSelected -> {
                viewModelScope.launch {
                    _exercises.value = getExercisesByMuscleId(event.muscle.muscleId)
                    _currentMuscle.value = event.muscle
                    _currentExercise.value = null
                }
            }
            is AddEditTrainingBlockScreenEvent.ExerciseSelected -> {
                _currentExercise.value = event.exercise
            }
            is AddEditTrainingBlockScreenEvent.RepsEntered -> {
                val newList = _validateSets.value.toMutableList()
                newList[event.index] = newList[event.index].copy(
                    setData = newList[event.index].setData.copy(
                        repeats = if (event.value.isNotEmpty() && event.value.isDigitsOnly()) event.value.toInt() else null
                    )
                )
                newList[event.index] = newList[event.index].copy(
                    repsError = false
                )
                _validateSets.value = newList
            }
            is AddEditTrainingBlockScreenEvent.WeightEntered -> {
                val newList = _validateSets.value.toMutableList()
                newList[event.index] = newList[event.index].copy(
                    setData = newList[event.index].setData.copy(
                        weight = if (event.value.isNotEmpty() && event.value.isDigitsOnly()) event.value.toInt() else null
                    )
                )
                newList[event.index] = newList[event.index].copy(
                    weightError = false
                )
                _validateSets.value = newList
            }
            is AddEditTrainingBlockScreenEvent.SaveChosen -> {
                if (validateList()) {
                    viewModelScope.launch {
                        insertTrainingBlock(
                            TrainingBlock(
                                currentTrainingBlockId,
                                setOrder!!,
                                currentTrainingId,
                                currentExercise.value!!.exerciseId
                            ),
                            _validateSets.value.map { it.setData }
                        )
                        _eventFlow.emit(UiEvent.SavePressed)
                    }
                }
            }

            AddEditTrainingBlockScreenEvent.DeleteChosen -> {
                viewModelScope.launch {
                    deleteTrainingBlock(currentTrainingBlockId)
                    _eventFlow.emit(UiEvent.DeletePressed)
                }
            }
            is AddEditTrainingBlockScreenEvent.ExerciseEntered -> {
                if (event.exerciseName.isBlank()) {
                    _currentExercise.value = null
                }
            }
            is AddEditTrainingBlockScreenEvent.MuscleEntered -> {
                if (event.muscleName.isBlank()) {
                    _currentMuscle.value = null
                    _currentExercise.value = null
                    _exercises.value = listOf()
                }
            }
        }
    }

    private fun validateList(): Boolean {
        if(_validateSets.value.isEmpty()) return false
        if(currentMuscle.value == null || currentExercise.value == null) return false
        val newList = _validateSets.value.toMutableList()
        var dataIsCorrect = true
        when (currentExercise.value?.exerciseType) {
            ExerciseType.REPS -> {
                for (i in newList.indices) {
                    if (newList[i].setData.repeats == null) {
                        dataIsCorrect = false
                        newList[i] = newList[i].copy(repsError = true)
                    }
                }
            }
            ExerciseType.WEIGHT_AND_REPS -> {
                for (i in newList.indices) {
                    if (newList[i].setData.repeats == null) {
                        dataIsCorrect = false
                        newList[i] = newList[i].copy(repsError = true)
                    }
                    if (newList[i].setData.weight == null) {
                        dataIsCorrect = false
                        newList[i] = newList[i].copy(weightError = true)
                    }
                }
            }
        }
        _validateSets.value = newList
        return dataIsCorrect
    }

    sealed class UiEvent {
        object SavePressed : UiEvent()
        object DeletePressed : UiEvent()
    }
}