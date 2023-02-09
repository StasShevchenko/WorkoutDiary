package com.example.workoutdiary.presentation.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.domain.model.TrainingDay
import com.example.workoutdiary.domain.use_case.trainings_use_cases.GetTrainingsByMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TrainingsViewModel @Inject constructor(
    val trainingUseCase: GetTrainingsByMonth
) : ViewModel() {

    private val _trainingDaysList: MutableStateFlow<List<TrainingDay>> =
        MutableStateFlow(emptyList())
    val trainingDaysList: StateFlow<List<TrainingDay>> = _trainingDaysList

    var todayTraining: Training? = null

    private var trainingDaysJob: Job? = null

    private fun getTrainingDaysByMonth(date: LocalDate) {
        trainingDaysJob?.cancel()
        trainingDaysJob = viewModelScope.launch {
            _trainingDaysList.subscriptionCount.collectLatest {
                if(it > 0) {
                    trainingUseCase(date)
                        .collectLatest { trainings ->
                            val cacheList = mutableListOf<TrainingDay>()
                            var i = 1
                            while (i <= date.lengthOfMonth()) {
                                cacheList.add(
                                    TrainingDay(
                                        LocalDate.of(
                                            date.year,
                                            date.month.value,
                                            i
                                        )
                                    )
                                )
                                i++
                            }
                            for (training: Training in trainings) {
                                val index = cacheList.indexOf(cacheList.find { trainingDay ->
                                    trainingDay.date.year == training.trainingDate.year && trainingDay.date.month == training.trainingDate.month
                                            && trainingDay.date.dayOfMonth == training.trainingDate.dayOfMonth
                                })
                                if (index != -1) {
                                    cacheList[index] = cacheList[index].copy(training = training)
                                }
                            }
                            _trainingDaysList.value = cacheList
                        }
                }
            }
        }

    }

    init {
        getTrainingDaysByMonth(LocalDate.now())
        viewModelScope.launch {
            trainingUseCase(LocalDate.now()).collectLatest{ trainings ->
                val currentDate = LocalDate.now()
                trainings.find { training ->
                    currentDate.year == training.trainingDate.year && currentDate.month == training.trainingDate.month
                            && currentDate.dayOfMonth == training.trainingDate.dayOfMonth
                }?.let { training ->
                    todayTraining = training
                }
            }
        }
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.ChangeDate -> {
                getTrainingDaysByMonth(event.date)
            }
        }
    }
}
