package com.example.workoutdiary.presentation.add_edit_training_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.data.model.entities.TrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ExerciseTrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import com.example.workoutdiary.domain.use_case.training_detailse_use_cases.GetTrainingDetailsByTrainingID
import com.example.workoutdiary.domain.use_case.training_detailse_use_cases.InsertTrainingBlock
import com.example.workoutdiary.domain.use_case.training_detailse_use_cases.UpdateTrainingBlocks
import com.example.workoutdiary.domain.use_case.trainings_use_cases.DeleteTraining
import com.example.workoutdiary.domain.use_case.trainings_use_cases.InsertTraining
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
    private val updateTrainingBlocks: UpdateTrainingBlocks,
    private val insertTrainingBlock: InsertTrainingBlock,
    private val state: SavedStateHandle
) : ViewModel() {


    private val _trainingDetails: MutableStateFlow<List<Pair<ExerciseTrainingBlock, List<ParameterizedSet>>>> =
        MutableStateFlow(listOf())
    val trainingDetails: StateFlow<List<Pair<ExerciseTrainingBlock, List<ParameterizedSet>>>> =
        _trainingDetails

    private val swappedBlocks: MutableList<TrainingBlock> = mutableListOf()

    // this var is used to know can user navigate back, or he needs to wait new entry inserting
    private var isInserted = false

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
        val isFromFavourite: Boolean = state.get<Boolean>("isFromFavourites")!!
        unchangedTrainingName = trainingName
        if (isFromFavourite) {
            viewModelScope.launch {
                val trainingBlocks = trainingDetailsUseCase(currentTrainingId).first()
                currentTrainingId = insertTrainingUseCase(
                    Training(
                        0,
                        trainingName = trainingName,
                        trainingDate = date,
                        isFavourite = true
                    )
                ).toInt()
                trainingBlocks.forEach { entry ->
                    insertTrainingBlock(entry.key.mapToTrainingBlock().copy(trainingId = currentTrainingId), entry.value)
                }
                isInserted = true
                trainingDetailsUseCase(currentTrainingId)
                    .distinctUntilChanged()
                    .collectLatest { trainingDetails ->
                        swappedBlocks.clear()
                        _trainingDetails.value = trainingDetails.toList()
                        trainingDetails.toList().forEach{
                            swappedBlocks.add(it.first.mapToTrainingBlock())
                        }
                    }
            }
        } else {
            if (currentTrainingId == -1) {
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
                            swappedBlocks.clear()
                            _trainingDetails.value = trainingDetails.toList()
                            trainingDetails.toList().forEach{
                                swappedBlocks.add(it.first.mapToTrainingBlock())
                            }
                        }
                }
            } else {
                isInserted = true
                viewModelScope.launch {
                    trainingDetailsUseCase(currentTrainingId)
                        .distinctUntilChanged()
                        .collectLatest { trainingDetails ->
                            swappedBlocks.clear()
                            _trainingDetails.value = trainingDetails.toList()
                            trainingDetails.toList().forEach{
                                swappedBlocks.add(it.first.mapToTrainingBlock())
                            }
                        }
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
                        if (trainingDetails.value.isEmpty()) {
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
            is AddEditTrainingScreenEvent.TrainingBlockSwapped -> {
                    val firstTrainingBlock = swappedBlocks[event.fromPosition].copy(trainingBlockOrder = swappedBlocks[event.toPosition].trainingBlockOrder)
                    val secondTrainingBlock = swappedBlocks[event.toPosition].copy(trainingBlockOrder = swappedBlocks[event.fromPosition].trainingBlockOrder)
                    swappedBlocks[event.toPosition] = firstTrainingBlock
                    swappedBlocks[event.fromPosition] = secondTrainingBlock
            }
            AddEditTrainingScreenEvent.SwapFinished -> {
                viewModelScope.launch {
                    updateTrainingBlocks(swappedBlocks)
                }
            }
        }
    }

    sealed class UiEvent {
        object OnBackPressed : UiEvent()
        object OnDeletePressed : UiEvent()
    }
}