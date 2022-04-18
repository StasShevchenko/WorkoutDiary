package com.example.workoutdiary.presentation.home_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.domain.model.TrainingDay
import com.example.workoutdiary.domain.use_case.GetTrainingsByMonth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
val trainingUseCase: GetTrainingsByMonth
) : ViewModel() {

    private val _trainingDaysList: MutableLiveData<List<TrainingDay>> = MutableLiveData(emptyList())
    val trainingDaysList: LiveData<List<TrainingDay>> = _trainingDaysList


    private var getTrainingDaysJob: Job? = null


    private fun getTrainingDaysByMonth(date: LocalDate) {
        getTrainingDaysJob?.cancel()
        getTrainingDaysJob = trainingUseCase(date).onEach { trainings ->
            val cacheList = mutableListOf<TrainingDay>()
            var i = 1
            while (i <= date.lengthOfMonth()) {
                cacheList.add( TrainingDay(LocalDate.of(date.year, date.month.value, i)))
                i++
            }
            for (training: Training in trainings) {
               val index = cacheList.indexOf(cacheList.find { trainingDay ->
                   trainingDay.date.year == training.trainingDate.year &&  trainingDay.date.month == training.trainingDate.month
                           && trainingDay.date.dayOfMonth == training.trainingDate.dayOfMonth
               })
                if (index != -1) {
                    cacheList[index].trainingList.add(training)
                }
            }
            _trainingDaysList.value = cacheList
        }.launchIn(viewModelScope)

    }

    init {
        getTrainingDaysByMonth(LocalDate.now())
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event){
            is HomeScreenEvent.ChangeDate -> {
                getTrainingDaysByMonth(event.date)
            }
        }
    }
}