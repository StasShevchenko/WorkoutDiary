package com.example.workoutdiary.presentation.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutdiary.domain.use_case.statistic_use_cases.GetTotalRepsCount
import com.example.workoutdiary.domain.use_case.statistic_use_cases.GetTotalSetsCount
import com.example.workoutdiary.domain.use_case.statistic_use_cases.GetTotalTrainingsCount
import com.example.workoutdiary.domain.use_case.statistic_use_cases.GetTotalWeightCount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    private val getTotalTrainingsCount: GetTotalTrainingsCount,
    private val getTotalSetsCount: GetTotalSetsCount,
    private val getTotalRepsCount: GetTotalRepsCount,
    private val getTotalWeightCount: GetTotalWeightCount
) : ViewModel() {

    private val _totalTrainingsCount: MutableStateFlow<Int?> = MutableStateFlow(0)
    val totalTrainingsCount: StateFlow<Int?> = _totalTrainingsCount

    private val _totalSetsCount: MutableStateFlow<Int?> = MutableStateFlow(0)
    val totalSetsCount: StateFlow<Int?> = _totalSetsCount

    private val _totalRepsCount: MutableStateFlow<Int?> = MutableStateFlow(0)
    val totalRepsCount: StateFlow<Int?> = _totalRepsCount

    private val _totalWeightCount: MutableStateFlow<Int?> = MutableStateFlow(0)
    val totalWeightCount: StateFlow<Int?> = _totalWeightCount


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
                totalRepsCount?.let {
                    _totalRepsCount.value = totalRepsCount
                }
            }
        }
        viewModelScope.launch {
            getTotalWeightCount().collectLatest { totalWeightCount ->
                totalWeightCount?.let {
                    _totalWeightCount.value = totalWeightCount
                }
            }
        }
    }
}