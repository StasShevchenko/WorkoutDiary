package com.example.workoutdiary.presentation.add_exercises_and_muscles_screen.exercises_screen.add_exercise_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.data.model.entities.Muscle
import com.example.workoutdiary.domain.use_case.exercise_use_cases.AddExercise
import com.example.workoutdiary.domain.use_case.exercise_use_cases.GetExercise
import com.example.workoutdiary.domain.use_case.muscles_use_cases.GetMuscle
import com.example.workoutdiary.domain.use_case.muscles_use_cases.GetMuscles
import com.example.workoutdiary.domain.use_case.exercise_use_cases.GetExerciseByName
import com.example.workoutdiary.utils.ExerciseType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExerciseScreenViewModel @Inject constructor(
    private val addExercise: AddExercise,
    private val getExercise: GetExercise,
    private val getMuscle: GetMuscle,
    private val getMuscles: GetMuscles,
    private val getExerciseByName: GetExerciseByName,
    savedStateHandle: SavedStateHandle,
    ) : ViewModel() {

    private val _currentExercise: MutableStateFlow<Exercise?> = MutableStateFlow(null)
    val currentExercise: StateFlow<Exercise?> = _currentExercise

    private val _muscles: MutableStateFlow<List<Muscle>> = MutableStateFlow(listOf())
    val muscles: StateFlow<List<Muscle>> = _muscles

    var currentMuscle: Muscle? = null
    private set

    private var exerciseDescription: String? = null

    private var exerciseName: String? = null

    private var exerciseType: ExerciseType = ExerciseType.REPS

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>("exerciseId")?.let { id ->
            viewModelScope.launch {
                val exercise = getExercise(id)
                exerciseName = exercise.exerciseName
                exerciseType = exercise.exerciseType
                exerciseDescription = exercise.exerciseDescription
                currentMuscle = getMuscle(exercise.muscleId)
                _currentExercise.value = exercise
            }
        }
        viewModelScope.launch {
            getMuscles().collectLatest { muscles ->
                _muscles.value = muscles
            }
        }
    }


    fun onEvent(event: AddExercisesScreenEvent) {
        when (event) {
            is AddExercisesScreenEvent.ExerciseAdded -> {
                if (currentExercise.value == null) {
                    if (exerciseName.isNullOrEmpty() || currentMuscle == null) {
                        viewModelScope.launch {
                            _eventFlow.emit(UiEvent.ValidationFailed)
                        }
                    }
                    else{
                        viewModelScope.launch {
                            val checkExercise = getExerciseByName(exerciseName!!)
                            if (checkExercise == null) {
                                val exercise = Exercise(
                                    exerciseId = 0,
                                    exerciseName = exerciseName!!,
                                    exerciseType = exerciseType,
                                    exerciseDescription = exerciseDescription,
                                    muscleId = currentMuscle!!.muscleId
                                )
                                addExercise(exercise)
                                _eventFlow.emit(UiEvent.AddPressed)
                            }
                            else{
                               _eventFlow.emit(UiEvent.ExerciseAlreadyExists)
                            }
                        }
                    }
                } else{
                    if (exerciseName.isNullOrEmpty()) {
                        viewModelScope.launch {
                            _eventFlow.emit(UiEvent.ValidationFailed)
                        }
                    } else{
                        viewModelScope.launch {
                            _eventFlow.emit(UiEvent.UpdatePressed)
                        }
                    }
                }
            }
            is AddExercisesScreenEvent.ExerciseDescriptionEntered -> {
                exerciseDescription = event.description.ifEmpty {
                    null
                }
            }
            is AddExercisesScreenEvent.ExerciseNameEntered -> {
                exerciseName = event.exerciseName
            }
            is AddExercisesScreenEvent.ExerciseTypeSelected -> {
                exerciseType = event.exerciseType
            }
            is AddExercisesScreenEvent.MuscleSelected -> {
                currentMuscle = event.muscle
            }
            AddExercisesScreenEvent.UpdateConfirmed -> {
                viewModelScope.launch {
                    val exercise = Exercise(
                        exerciseId = currentExercise.value!!.exerciseId,
                        exerciseName = exerciseName!!,
                        exerciseType = exerciseType,
                        exerciseDescription = exerciseDescription,
                        muscleId = currentMuscle!!.muscleId
                    )
                    addExercise(exercise)
                    _eventFlow.emit(UiEvent.AddPressed)
                }
            }
        }
    }

    sealed class UiEvent{
        object AddPressed : UiEvent()
        object UpdatePressed : UiEvent()
        object ValidationFailed : UiEvent()
        object ExerciseAlreadyExists : UiEvent()
    }
}