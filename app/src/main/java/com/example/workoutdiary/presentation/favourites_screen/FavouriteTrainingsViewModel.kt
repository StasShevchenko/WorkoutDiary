package com.example.workoutdiary.presentation.favourites_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.domain.use_case.trainings_use_cases.ClearFavouriteTrainingsByName
import com.example.workoutdiary.domain.use_case.trainings_use_cases.GetAllFavouriteTrainings
import com.example.workoutdiary.domain.use_case.trainings_use_cases.RestoreFavouriteTrainingsByName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteTrainingsViewModel @Inject constructor(
    private val getAllFavouriteTrainings: GetAllFavouriteTrainings,
    private val clearFavouriteTrainingsByName: ClearFavouriteTrainingsByName,
    private val restoreFavouriteTrainingsByName: RestoreFavouriteTrainingsByName
) : ViewModel() {
    private val _favouriteTrainings: MutableStateFlow<List<Training>?> = MutableStateFlow(null)
    val favouriteTrainings: StateFlow<List<Training>?> = _favouriteTrainings

    private var lastDeletedFavouriteTrainingsName = ""

    private val _eventFlow: MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val eventFlow: SharedFlow<UiEvent> = _eventFlow

    fun deleteFavouriteTrainings(trainingsName: String) {
        viewModelScope.launch {
            lastDeletedFavouriteTrainingsName = trainingsName
            clearFavouriteTrainingsByName(trainingsName)
            _eventFlow.emit(UiEvent.TrainingsDeleted(trainingsName))
        }
    }

    fun restoreFavouriteTrainings(trainingsName: String) {
        viewModelScope.launch {
            restoreFavouriteTrainingsByName(lastDeletedFavouriteTrainingsName)
        }
    }

    init {
        viewModelScope.launch {
            getAllFavouriteTrainings().collectLatest {favouriteTrainings ->
                _favouriteTrainings.value = favouriteTrainings
            }
        }
    }

    sealed class UiEvent {
        data class TrainingsDeleted(val trainingsName: String) : UiEvent()
    }
}