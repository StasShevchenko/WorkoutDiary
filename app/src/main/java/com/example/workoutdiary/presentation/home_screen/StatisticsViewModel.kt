package com.example.workoutdiary.presentation.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdiary.data.model.entities.ExerciseStatisticsParameters
import com.example.workoutdiary.domain.use_case.statistic_use_cases.*
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getTotalTrainingsCount: GetTotalTrainingsCount,
    private val getTotalSetsCount: GetTotalSetsCount,
    private val getTotalRepsCount: GetTotalRepsCount,
    private val getTotalWeightCount: GetTotalWeightCount,
    private val getExerciseStatisticsInfo: GetExerciseStatisticsInfo,
    private val getExerciseStatisticsParameters: GetExerciseStatisticsParameters
) : ViewModel() {

    private val _totalTrainingsCount: MutableStateFlow<Int?> = MutableStateFlow(0)
    val totalTrainingsCount: StateFlow<Int?> = _totalTrainingsCount

    private val _totalSetsCount: MutableStateFlow<Int?> = MutableStateFlow(0)
    val totalSetsCount: StateFlow<Int?> = _totalSetsCount

    private val _totalRepsCount: MutableStateFlow<Int?> = MutableStateFlow(0)
    val totalRepsCount: StateFlow<Int?> = _totalRepsCount

    private val _totalWeightCount: MutableStateFlow<Int?> = MutableStateFlow(0)
    val totalWeightCount: StateFlow<Int?> = _totalWeightCount

    private val _statisticsInfo: MutableStateFlow<Pair<String, Map<LocalDate, Int>>?> = MutableStateFlow(null)
    val statisticsInfo: StateFlow<Pair<String, Map<LocalDate, Int>>?> = _statisticsInfo

    private val _statisticsParameters: MutableStateFlow<ExerciseStatisticsParameters?> = MutableStateFlow(null)
    val statisticsParameters: StateFlow<ExerciseStatisticsParameters?> = _statisticsParameters

    val statisticsEntryProducer = ChartEntryModelProducer(getStatisticsEntries())

    init {
        viewModelScope.launch {
            getTotalTrainingsCount().collectLatest { totalTrainingsCount ->
                _totalTrainingsCount.value = totalTrainingsCount
            }
        }
        viewModelScope.launch {
            getTotalSetsCount().collectLatest { totalSetsCount ->
                _totalSetsCount.value = totalSetsCount
            }
        }
        viewModelScope.launch {
            getTotalRepsCount().collectLatest { totalRepsCount ->
                _totalRepsCount.value = totalRepsCount ?: 0
            }
        }
        viewModelScope.launch {
            getTotalWeightCount().collectLatest { totalWeightCount ->
                _totalWeightCount.value = totalWeightCount ?: 0
            }
        }
        viewModelScope.launch {
            getExerciseStatisticsInfo().collectLatest { statisticsInfo ->
                _statisticsInfo.value = statisticsInfo
                statisticsEntryProducer.setEntries(getStatisticsEntries())
            }
        }
        viewModelScope.launch {
            getExerciseStatisticsParameters().collectLatest { parameters ->
                _statisticsParameters.value = parameters
            }
        }
    }

    private fun getStatisticsEntries(): List<DateEntry> {
        val dateEntryList: MutableList<DateEntry> = mutableListOf()
        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy")
        var x = 0
        statisticsInfo.value?.second?.forEach { statisticsEntry ->
            dateEntryList.add(DateEntry(
                date = statisticsEntry.key.format(dateFormatter),
                x = x.toFloat(),
                y = statisticsEntry.value.toFloat()
            ))
            x++
        }
        return dateEntryList
    }
}