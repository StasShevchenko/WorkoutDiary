package com.example.workoutdiary.presentation.home_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.domain.model.TrainingDay
import com.example.workoutdiary.domain.use_case.GetTrainingsByMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    val trainingUseCase: GetTrainingsByMonth
) : ViewModel() {

    private val _trainingDaysList: MutableStateFlow<List<TrainingDay>> =
        MutableStateFlow(emptyList())
    val trainingDaysList: StateFlow<List<TrainingDay>> = _trainingDaysList

    var currentItem: Training? = null

    private var trainingDaysJob: Job? = null

    private fun getTrainingDaysByMonth(date: LocalDate) {
        trainingDaysJob?.cancel()
        trainingDaysJob = viewModelScope.launch {
            _trainingDaysList.subscriptionCount.collectLatest {
                if(it > 0) {
                    trainingUseCase(date)
                        .collectLatest { trainings ->
                            currentItem = null
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
                                if (training.trainingDate == LocalDate.now())
                                    currentItem = training
                                val index = cacheList.indexOf(cacheList.find { trainingDay ->
                                    trainingDay.date.year == training.trainingDate.year && trainingDay.date.month == training.trainingDate.month
                                            && trainingDay.date.dayOfMonth == training.trainingDate.dayOfMonth
                                })
                                if (index != -1) {
                                    cacheList[index].trainingList.add(training)
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
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.ChangeDate -> {
                getTrainingDaysByMonth(event.date)
            }
        }
    }
}
