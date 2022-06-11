package com.example.workoutdiary.presentation.add_edit_training_screen

import android.os.Looper
import android.util.Log
import androidx.lifecycle.*
import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.data.model.relation_entities.ExerciseTrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import com.example.workoutdiary.domain.use_case.DeleteTraining
import com.example.workoutdiary.domain.use_case.GetTrainingDetailsByTrainingID
import com.example.workoutdiary.domain.use_case.InsertTraining
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AddEditTrainingScreenViewModel @Inject constructor(
    private val deleteTrainingUseCase: DeleteTraining,
    private val insertTrainingUseCase: InsertTraining,
    private val trainingDetailsUseCase: GetTrainingDetailsByTrainingID,
    private val state: SavedStateHandle
) : ViewModel() {

    val _trainingDetails: MutableStateFlow<Map<ExerciseTrainingBlock, List<ParameterizedSet>>> =
       MutableStateFlow(mapOf())
    val trainingDetails: StateFlow<Map<ExerciseTrainingBlock, List<ParameterizedSet>>> = _trainingDetails

    // this var is used to know can user navigate back, or he need to wait new entry inserting
    private var isInserted = false

    //this var is used to know can we display details when we insert new entry
    var isNewEntryReceived = false
    private set

    //this var is used to know can we display details for existing entry
    var isCurrentItemReceived = false
    private set

    var trainingName = ""
        private set

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var date: LocalDate
        private set

    private var currentTrainingId = -1

    init {
        date = state.get<LocalDate>("trainingDate")!!
        currentTrainingId = state.get<Int>("trainingId")!!
        trainingName = state.get<String>("trainingName") ?: ""
        if (currentTrainingId == -1) {
            isNewEntryReceived = true
            viewModelScope.launch {
                currentTrainingId = insertTrainingUseCase(
                    Training(
                        0,
                        trainingName = trainingName,
                        trainingDate = date
                    )
                ).toInt()
                isInserted = true
                trainingDetailsUseCase(currentTrainingId).collectLatest { trainingDetails ->
                    _trainingDetails.value = trainingDetails
                }
            }
        } else {
            isInserted = true
            isCurrentItemReceived = true
            viewModelScope.launch {
                trainingDetailsUseCase(currentTrainingId).collectLatest { trainingDetails ->
                    _trainingDetails.value = trainingDetails
                }
            }
        }
    }

    fun onEvent(event: AddEditTrainingScreenEvent) {
        when (event) {
            is AddEditTrainingScreenEvent.NameEntered -> {
                trainingName = event.text
            }
            AddEditTrainingScreenEvent.OnBackPressed -> {
                if (isInserted) {
                    viewModelScope.launch {
                        if (trainingDetails.value.keys.isEmpty()) {
                            deleteTrainingUseCase(currentTrainingId)
                        }
                        _eventFlow.emit(
                            UiEvent.OnBackPressed
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        object OnBackPressed : UiEvent()
    }
}