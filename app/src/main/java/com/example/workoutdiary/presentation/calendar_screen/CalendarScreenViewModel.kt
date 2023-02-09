package com.example.workoutdiary.presentation.calendar_screen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.data.model.relation_entities.ExerciseTrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import com.example.workoutdiary.domain.use_case.training_detailse_use_cases.GetTrainingDetailsByTrainingID
import com.example.workoutdiary.domain.use_case.trainings_use_cases.GetTrainingsByMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarScreenViewModel @Inject constructor(
    private val trainings: GetTrainingsByMonth,
    private val trainingDetails: GetTrainingDetailsByTrainingID
) : ViewModel() {

    var selectedDate: LocalDate = LocalDate.now()
        private set

    private val _trainingsList: MutableStateFlow<List<Training?>> = MutableStateFlow(listOf())
    val trainingsList: StateFlow<List<Training?>> = _trainingsList

    private val _currentTraining: MutableStateFlow<Training?> = MutableStateFlow(null)
    val currentTraining: StateFlow<Training?> = _currentTraining

    private val _currentTrainingDetails: MutableStateFlow<Map<ExerciseTrainingBlock, List<ParameterizedSet>>> =
        MutableStateFlow(
            mapOf()
        )
    val currentTrainingDetails: StateFlow<Map<ExerciseTrainingBlock, List<ParameterizedSet>>> = _currentTrainingDetails


    var currentMonth: YearMonth = YearMonth.now()
        private set


    private var getTrainingsJob: Job? = null

    init {
        viewModelScope.launch {
            trainings(LocalDate.now()).collectLatest { trainingsList ->
                val mutableTrainingsList: MutableList<Training?> = mutableListOf()
                val yearMonth = YearMonth.of(LocalDate.now().year, LocalDate.now().monthValue)
                val currentMonthDaysCount = yearMonth.lengthOfMonth()
                for (i in 1..currentMonthDaysCount) {
                    val currentTraining: Training? = trainingsList.find { training ->
                        training.trainingDate.dayOfMonth == i
                    }
                    mutableTrainingsList.add(currentTraining)
                }
                _trainingsList.value = mutableTrainingsList
                _currentTraining.value = getCurrentTraining(LocalDate.now())
                getCurrentDetails()
            }
        }
    }

    fun onEvent(event: CalendarScreenEvent) {
        when (event) {
            is CalendarScreenEvent.DaySelected -> {
                selectedDate = event.day.date
                _currentTraining.value = getCurrentTraining(event.day.date)
                getCurrentDetails()
            }
            is CalendarScreenEvent.MonthChanged -> {
                currentMonth = YearMonth.of(event.date.year, event.date.monthValue)
                getTrainingsJob?.cancel()
                getTrainingsJob = viewModelScope.launch {
                    trainings(event.date).collectLatest { trainingsList ->
                        val mutableTrainingsList: MutableList<Training?> = mutableListOf()
                        val yearMonth = YearMonth.of(event.date.year, event.date.monthValue)
                        val currentMonthDaysCount = yearMonth.lengthOfMonth()
                        for (i in 1..currentMonthDaysCount) {
                            val currentTraining: Training? = trainingsList.find { training ->
                                training.trainingDate.dayOfMonth == i
                            }
                            mutableTrainingsList.add(currentTraining)
                        }
                        _trainingsList.value = mutableTrainingsList
                    }
                }
            }
        }
    }


    private fun getCurrentTraining(date: LocalDate): Training? {
        val trainingToFind: Training? = trainingsList.value.find { training ->
            training?.trainingDate == date
        }
        return trainingToFind
    }

    private fun getCurrentDetails(){
        viewModelScope.launch {
            currentTraining.value?.let {
                trainingDetails(it.trainingId).collectLatest { trainingDetails ->
                    _currentTrainingDetails.value = trainingDetails
                }
            }
        }
    }
}
