package com.example.workoutdiary.presentation.edit_statistics_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.data.model.entities.ExerciseStatisticsParameters
import com.example.workoutdiary.domain.use_case.exercise_use_cases.GetExercisesByName
import com.example.workoutdiary.domain.use_case.statistic_use_cases.UpdateExerciseStatisticsInfo
import com.example.workoutdiary.utils.ExerciseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditStatisticsScreenViewModel @Inject constructor(
    private val getExercises: GetExercisesByName,
    private val updateExerciseStatisticsInfo: UpdateExerciseStatisticsInfo
) : ViewModel(){

    private val _exercises: MutableStateFlow<List<Exercise>> = MutableStateFlow(listOf())
    val exercises: StateFlow<List<Exercise>> = _exercises

    private val _currentExercise: MutableStateFlow<Exercise?> = MutableStateFlow(null)
    val currentExercise: StateFlow<Exercise?> = _currentExercise

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

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

    fun savePressed(){
        viewModelScope.launch {
            if (currentExercise.value == null) {
                _eventFlow.emit(UiEvent.ValidationFailed)
            } else {
                updateExerciseStatisticsInfo(
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
            }
        }
    }

    sealed class UiEvent{
        object ValidationFailed : UiEvent()
        object SavePressed : UiEvent()
    }

}