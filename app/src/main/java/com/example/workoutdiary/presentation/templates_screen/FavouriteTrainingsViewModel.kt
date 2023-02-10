package com.example.workoutdiary.presentation.templates_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.domain.use_case.trainings_use_cases.ClearFavouriteTrainingsByName
import com.example.workoutdiary.domain.use_case.trainings_use_cases.GetAllFavouriteTrainings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteTrainingsViewModel @Inject constructor(
    private val getAllFavouriteTrainings: GetAllFavouriteTrainings,
    private val clearFavouriteTrainingsByName: ClearFavouriteTrainingsByName
) : ViewModel(){
    private val _favouriteTrainings: MutableStateFlow<List<Training>?> = MutableStateFlow(null)
    val favouriteTrainings: StateFlow<List<Training>?> = _favouriteTrainings

    init {
        viewModelScope.launch {
            _favouriteTrainings.value = getAllFavouriteTrainings()
        }
    }
}