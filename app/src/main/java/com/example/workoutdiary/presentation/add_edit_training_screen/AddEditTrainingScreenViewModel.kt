package com.example.workoutdiary.presentation.add_edit_training_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.data.model.relation_entities.ExerciseTrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import com.example.workoutdiary.domain.use_case.DeleteTraining
import com.example.workoutdiary.domain.use_case.GetTrainingDetailsByTrainingID
import com.example.workoutdiary.domain.use_case.InsertTraining
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
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
    val trainingDetails: StateFlow<Map<ExerciseTrainingBlock, List<ParameterizedSet>>> =
        _trainingDetails

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

    private var unchangedTrainingName: String

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var date: LocalDate
        private set

    var currentTrainingId = -1
        private set

    init {
        date = state.get<LocalDate>("trainingDate")!!
        currentTrainingId = state.get<Int>("trainingId")!!
        trainingName = state.get<String>("trainingName") ?: ""
        unchangedTrainingName = trainingName
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
                trainingDetailsUseCase(currentTrainingId)
                    .distinctUntilChanged()
                    .collectLatest { trainingDetails ->
                        _trainingDetails.value = trainingDetails
                    }
            }
        } else {
            isInserted = true
            isCurrentItemReceived = true
            viewModelScope.launch {
                trainingDetailsUseCase(currentTrainingId)
                    .distinctUntilChanged()
                    .collectLatest { trainingDetails ->
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
            is AddEditTrainingScreenEvent.OnBackPressed -> {
                if (isInserted) {
                    viewModelScope.launch {
                        if (trainingDetails.value.keys.isEmpty()) {
                            deleteTrainingUseCase(currentTrainingId)
                        } else if (trainingName != unchangedTrainingName) {
                            insertTrainingUseCase(
                                Training(
                                    currentTrainingId,
                                    trainingName,
                                    date
                                )
                            )
                        }
                        _eventFlow.emit(
                            UiEvent.OnBackPressed
                        )
                    }
                }
            }
            is AddEditTrainingScreenEvent.DeletePressed -> {
                viewModelScope.launch {
                deleteTrainingUseCase(currentTrainingId)
                    _eventFlow.emit(UiEvent.OnDeletePressed)
                }
            }
        }
    }

    sealed class UiEvent {
        object OnBackPressed : UiEvent()
        object OnDeletePressed : UiEvent()
    }
}