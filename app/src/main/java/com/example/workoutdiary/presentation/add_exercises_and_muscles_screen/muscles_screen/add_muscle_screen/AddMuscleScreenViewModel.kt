package com.example.workoutdiary.presentation.add_exercises_and_muscles_screen.muscles_screen.add_muscle_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdiary.data.model.entities.Muscle
import com.example.workoutdiary.domain.use_case.muscles_use_cases.AddMuscle
import com.example.workoutdiary.domain.use_case.muscles_use_cases.GetMuscle
import com.example.workoutdiary.domain.use_case.muscles_use_cases.GetMuscleByName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMuscleScreenViewModel @Inject constructor(
    private val addMuscle: AddMuscle,
    private val getMuscle: GetMuscle,
    private val getMuscleByName: GetMuscleByName,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _currentMuscle: MutableStateFlow<Muscle?> = MutableStateFlow(null)
    val currentMuscle: StateFlow<Muscle?> = _currentMuscle


    private var muscleName: String? = null

    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow.asSharedFlow()

    private val _muscleNameError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val muscleNameError: StateFlow<Boolean> = _muscleNameError


    init {
        savedStateHandle.get<Int>("muscleId")?.let { muscleId ->
            viewModelScope.launch {
                _currentMuscle.value = getMuscle(muscleId)
                muscleName = currentMuscle.value!!.muscleName
            }
        }
    }

    fun updateMuscleName(muscleName: String) {
        this.muscleName = muscleName.ifEmpty {
            null
        }
    }

    fun addMuscle() {
        if (_currentMuscle.value == null) {
            if (muscleName != null) {
                viewModelScope.launch {
                    val checkMuscle = getMuscleByName(muscleName!!)
                    if (checkMuscle == null) {
                        addMuscle(
                            Muscle(
                                muscleId = 0,
                                muscleName = muscleName!!
                            )
                        )
                        _eventFlow.emit(UiEvent.MuscleAdded)
                    } else {
                        _eventFlow.emit(UiEvent.MuscleAlreadyExists)
                    }
                }
            } else {
                _muscleNameError.value = true
            }
        } else {
            if (muscleName != null) {
                viewModelScope.launch {
                    addMuscle(
                        Muscle(
                            muscleId = currentMuscle.value!!.muscleId,
                            muscleName = muscleName!!
                        )
                    )
                    _eventFlow.emit(UiEvent.MuscleAdded)
                }
            } else{
                _muscleNameError.value = true
            }
        }

    }

    sealed class UiEvent {
        object MuscleAlreadyExists : UiEvent()
        object MuscleAdded : UiEvent()
    }

}