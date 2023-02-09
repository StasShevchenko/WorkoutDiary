package com.example.workoutdiary.presentation.edit_statistics_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.data.model.entities.ExerciseStatisticsParameters
import com.example.workoutdiary.domain.use_case.exercise_use_cases.GetExercisesByName
import com.example.workoutdiary.domain.use_case.statistic_use_cases.DeleteExerciseStatisticsParameters
import com.example.workoutdiary.domain.use_case.statistic_use_cases.GetExerciseStatisticsParameters
import com.example.workoutdiary.domain.use_case.statistic_use_cases.UpdateExerciseStatisticsParameters
import com.example.workoutdiary.utils.ExerciseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditStatisticsScreenViewModel @Inject constructor(
    private val getExercises: GetExercisesByName,
    private val updateExerciseStatisticsParameters: UpdateExerciseStatisticsParameters,
    private val getExerciseStatisticsParameters: GetExerciseStatisticsParameters,
    private val deleteExerciseStatisticsParameters: DeleteExerciseStatisticsParameters
) : ViewModel(){

    private val _exercises: MutableStateFlow<List<Exercise>> = MutableStateFlow(listOf())
    val exercises: StateFlow<List<Exercise>> = _exercises

    private val _currentExercise: MutableStateFlow<Exercise?> = MutableStateFlow(null)
    val currentExercise: StateFlow<Exercise?> = _currentExercise

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _currentExerciseStatisticsParameters: MutableStateFlow<ExerciseStatisticsParameters?> = MutableStateFlow(null)
    val currentExerciseStatisticsParameters: StateFlow<ExerciseStatisticsParameters?> = _currentExerciseStatisticsParameters


    var statisticsParameter = ""

    fun exerciseSelected(exercise: Exercise) {
        _currentExercise.value = exercise
        statisticsParameter = when (exercise.exerciseType) {
            ExerciseType.REPS -> "repeats"
            ExerciseType.WEIGHT_AND_REPS -> "repeats"
            ExerciseType.TIME -> "time"
            ExerciseType.DISTANCE -> "distance"
        }
    }

    fun resetCurrentParameters(){
        viewModelScope.launch {
            if (currentExerciseStatisticsParameters != null) {
                deleteExerciseStatisticsParameters(currentExerciseStatisticsParameters.value!!)
                _eventFlow.emit(UiEvent.SavePressed)
            }
        }
    }

    fun savePressed(){
        viewModelScope.launch {
            if (currentExercise.value == null) {
                _eventFlow.emit(UiEvent.ValidationFailed)
            } else {
                updateExerciseStatisticsParameters(
                    ExerciseStatisticsParameters(
                        exerciseName = currentExercise.value!!.exerciseName,
                        statisticsParameter = statisticsParameter
                    )
                )
                _eventFlow.emit(UiEvent.SavePressed)
            }
        }
    }

    init {
        viewModelScope.launch {
            getExercises("").collectLatest { exercises ->
                _exercises.value = exercises
                getExerciseStatisticsParameters().collectLatest {parameters ->
                    _currentExerciseStatisticsParameters.value = parameters
                    val chosenExercise = exercises.find {exercise ->
                        exercise.exerciseName == parameters?.exerciseName
                    }
                    if (chosenExercise != null) {
                        _currentExercise.value = chosenExercise
                    }
                }
            }
        }
    }

    sealed class UiEvent{
        object ValidationFailed : UiEvent()
        object SavePressed : UiEvent()
    }

}