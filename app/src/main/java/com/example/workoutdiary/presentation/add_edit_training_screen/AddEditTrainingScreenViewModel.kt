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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
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

     var trainingDetails: LiveData<Map<ExerciseTrainingBlock, List<ParameterizedSet>>>
     = trainingDetailsUseCase.invoke(-1).asLiveData()
    private set

    private var isInserted = false


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
        if(currentTrainingId == -1){
            viewModelScope.launch {
                val trainingDate = LocalDateTime.of(date, LocalTime.now())
                currentTrainingId = insertTrainingUseCase(
                    Training(
                        0,
                        trainingName = trainingName,
                        trainingDate = trainingDate
                    )
                ).toInt()
                trainingDetails = trainingDetailsUseCase.invoke(currentTrainingId).asLiveData()
                isInserted = true
            }
        }
        else {
            trainingDetails = trainingDetailsUseCase.invoke(currentTrainingId).asLiveData()
            isInserted = true
        }
    }

    fun onEvent(event: AddEditTrainingScreenEvent) {
        when (event) {
            is AddEditTrainingScreenEvent.NameEntered -> {
                trainingName = event.text
            }
            AddEditTrainingScreenEvent.OnBackPressed -> {
                if(isInserted) {
                    viewModelScope.launch {
                        if (trainingDetails.value?.keys.isNullOrEmpty()) {
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

    sealed class UiEvent{
        object OnBackPressed: UiEvent()
    }
}