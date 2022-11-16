package com.example.workoutdiary.presentation.add_exercises_and_muscles_screen.muscles_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdiary.data.model.entities.Muscle
import com.example.workoutdiary.domain.use_case.muscles_use_cases.AddMuscle
import com.example.workoutdiary.domain.use_case.muscles_use_cases.DeleteMuscle
import com.example.workoutdiary.domain.use_case.muscles_use_cases.GetMuscles
import com.example.workoutdiary.domain.use_case.muscles_use_cases.GetMusclesByName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusclesViewModel @Inject constructor(
    private val getMuscles: GetMusclesByName,
    private val deleteMuscleUseCase: DeleteMuscle,
) : ViewModel() {

    private val _muscles: MutableStateFlow<List<Muscle>> = MutableStateFlow(listOf())
    val muscles: StateFlow<List<Muscle>> = _muscles

    private var searchMusclesJob: Job? = null
    init {
        viewModelScope.launch {
            getMuscles("").collectLatest { muscles ->
                _muscles.value = muscles
            }
        }
    }

    fun deleteMuscle(muscle: Muscle) {
        viewModelScope.launch {
            deleteMuscleUseCase(muscle)
        }
    }

    fun searchMuscles(muscleName: String) {
        searchMusclesJob?.cancel()
        searchMusclesJob = viewModelScope.launch {
            delay(100)
            getMuscles(muscleName).collectLatest { muscles ->
                _muscles.value = muscles
            }
        }
    }



}
