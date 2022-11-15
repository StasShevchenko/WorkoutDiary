package com.example.workoutdiary.presentation.add_exercises_and_muscles_screen.exercises_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.domain.use_case.exercise_use_cases.DeleteExercise
import com.example.workoutdiary.domain.use_case.exercise_use_cases.GetExercisesByName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExercisesScreenViewModel @Inject constructor(
    private val getExercisesByName: GetExercisesByName,
    private val deleteExerciseUseCase: DeleteExercise
) : ViewModel() {

    private val _exercises: MutableStateFlow<List<Exercise>> = MutableStateFlow(listOf())
    val exercises: StateFlow<List<Exercise>> = _exercises

    private var searchExercisesCoroutineJob: Job? = null

    init {
        searchExercisesCoroutineJob = viewModelScope.launch {
            getExercisesByName("").collectLatest { exercises ->
                _exercises.value = exercises
            }
        }
    }


   fun searchExercises(searchQuery: String){
       searchExercisesCoroutineJob?.cancel()
       searchExercisesCoroutineJob = viewModelScope.launch {
           getExercisesByName(searchQuery).collectLatest { exercises ->
               _exercises.value = exercises
           }
       }
   }

    fun deleteExercise(exercise: Exercise) {
        viewModelScope.launch {
            deleteExerciseUseCase(exercise)
        }
    }

}