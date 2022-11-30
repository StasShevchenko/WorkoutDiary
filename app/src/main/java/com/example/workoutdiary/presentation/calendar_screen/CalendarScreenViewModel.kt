package com.example.workoutdiary.presentation.calendar_screen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.domain.use_case.GetTrainingsByMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.Month
import java.time.YearMonth
import javax.inject.Inject

@HiltViewModel
class CalendarScreenViewModel @Inject constructor(
    private val trainings: GetTrainingsByMonth
) : ViewModel() {

    var selectedDate: LocalDate = LocalDate.now()
        private set

    private val _trainingsList: MutableStateFlow<List<Training?>> = MutableStateFlow(
        mutableListOf()
    )

    var currentMonth: Month = LocalDate.now().month
    private set

    val trainingsList: StateFlow<List<Training?>> = _trainingsList

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
            }
        }
    }

    fun onEvent(event: CalendarScreenEvent) {
        when (event) {
            is CalendarScreenEvent.DaySelected -> {
                selectedDate = event.day.date
            }
            is CalendarScreenEvent.MonthChanged -> {
                currentMonth = event.date.month
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
}
