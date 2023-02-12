package com.example.workoutdiary.presentation.home_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutdiary.R
import com.example.workoutdiary.data.model.entities.ExerciseStatisticsParameters
import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.databinding.HomeScreenFragmentBinding
import com.example.workoutdiary.presentation.MainActivity
import com.example.workoutdiary.presentation.home_screen.chart_utils.*
import com.example.workoutdiary.presentation.utils.FabButtonClick
import com.patrykandpatrick.vico.core.axis.horizontal.HorizontalAxis
import com.patrykandpatrick.vico.core.axis.vertical.VerticalAxis
import com.patrykandpatrick.vico.core.scroll.InitialScroll
import com.patrykandpatrick.vico.views.scroll.copy
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class HomeScreenFragment : Fragment(R.layout.home_screen_fragment), FabButtonClick,
    TrainingDaysAdapter.OnTrainingClickListener {
    private val trainingsViewModel: TrainingsViewModel by viewModels()
    private val statisticsViewModel: StatisticsViewModel by viewModels()
    private lateinit var binding: HomeScreenFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as MainActivity
        activity.setFabListener(this)
        binding = HomeScreenFragmentBinding.bind(view)
        val trainingDaysAdapter = TrainingDaysAdapter(this)

        binding.apply {
            trainingRecyclerView.apply {
                adapter = trainingDaysAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }

            monthDatePicker.setOnDateChangedListener {
                trainingsViewModel.onEvent(HomeScreenEvent.ChangeDate(it))
            }

            monthDatePicker.setOnTextClickedAction {
                trainingRecyclerView.smoothScrollToPosition(LocalDate.now().dayOfMonth - 1)
            }

            emptyParametersMessageTextView.setOnClickListener {
                val action =
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToEditStatisticsScreenFragment()
                findNavController().navigate(action)
            }

            emptyDataMessageTextView.setOnClickListener {
                val action =
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToEditStatisticsScreenFragment()
                findNavController().navigate(action)
            }

            openStatisticsParametersButton.setOnClickListener {
                val action =
                    HomeScreenFragmentDirections.actionHomeScreenFragmentToEditStatisticsScreenFragment()
                findNavController().navigate(action)
            }

            with(chart) {
                entryProducer = statisticsViewModel.statisticsEntryProducer
                chartScrollSpec = chartScrollSpec.copy(initialScroll = InitialScroll.End)
                (bottomAxis as HorizontalAxis).valueFormatter =
                   DateValueFormatter()
                marker = getMarker()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    trainingsViewModel.trainingDaysList
                        .collectLatest {
                            trainingDaysAdapter.submitList(it)
                        }
                }
                launch {
                    statisticsViewModel.totalTrainingsCount.collectLatest { totalTrainingsCount ->
                        binding.totalTrainingsCountTextView.text =
                            "Всего тренировок: ${totalTrainingsCount}"
                    }
                }
                launch {
                    statisticsViewModel.totalSetsCount.collectLatest { totalSetsCount ->
                        binding.totalSetsCountTextView.text =
                            "Всего выполнено подходов: ${totalSetsCount}"
                    }
                }
                launch {
                    statisticsViewModel.totalRepsCount.collectLatest { totalRepsCount ->
                        binding.totalRepsCountTextView.text =
                            "Всего повторений в упражнениях: ${totalRepsCount}"
                    }
                }
                launch {
                    statisticsViewModel.totalWeightCount.collectLatest { totalWeightCount ->
                        binding.totalWeightCountTextView.text =
                            "Всего поднято: ${totalWeightCount} кг веса"
                    }
                }
                launch {
                    statisticsViewModel.statisticsInfo.collectLatest { statisticsValue ->
                        statisticsValue?.let {
                            binding.exerciseNameTextView.text = statisticsValue.first
                            updateStaticsVisibility(
                                statisticsValue.second,
                                statisticsViewModel.statisticsParameters.value
                            )
                        }
                    }
                }
                launch {
                    statisticsViewModel.statisticsParameters.collectLatest { parameters ->
                        updateStaticsVisibility(
                            statisticsViewModel.statisticsInfo.value?.second,
                            parameters
                        )
                        (binding.chart.startAxis as VerticalAxis).title =
                            when (parameters?.statisticsParameter) {
                                "repeats" -> "Повторения"
                                "weight" -> "Вес (кг)"
                                "time" -> "Время"
                                "distance" -> "Дистанция"
                                else -> "Повторения"
                            }
                        if (parameters?.statisticsParameter == "distance") {
                            (binding.chart.startAxis as VerticalAxis).valueFormatter =
                                DistanceValueFormatter()
                            val marker = getMarker()
                            marker.labelFormatter = DistanceMarkerLabelFormatter()
                            binding.chart.marker = marker
                        }
                        if (parameters?.statisticsParameter == "time") {
                            (binding.chart.startAxis as VerticalAxis).valueFormatter =
                                TimeValueFormatter()
                            val marker = getMarker()
                            marker.labelFormatter = TimeMarkerLabelFormatter()
                            binding.chart.marker = marker
                        }
                    }
                }
            }
        }
    }

    private fun updateStaticsVisibility(
        statisticsData: Map<LocalDate, Int>?,
        statisticsParameters: ExerciseStatisticsParameters?
    ) {
        if (statisticsParameters == null) {
            with(binding) {
                statisticsCardView.visibility = View.VISIBLE
                emptyParametersMessageTextView.visibility = View.VISIBLE
                emptyDataMessageTextView.visibility = View.GONE
                statisticsChartContainer.visibility = View.GONE
            }
        } else if (statisticsData == null || statisticsData.isEmpty()) {
            with(binding) {
                statisticsCardView.visibility = View.VISIBLE
                emptyParametersMessageTextView.visibility = View.GONE
                emptyDataMessageTextView.visibility = View.VISIBLE
                statisticsChartContainer.visibility = View.GONE
            }
        } else {
            with(binding) {
                statisticsCardView.visibility = View.VISIBLE
                emptyParametersMessageTextView.visibility = View.GONE
                emptyDataMessageTextView.visibility = View.GONE
                statisticsChartContainer.visibility = View.VISIBLE
            }
        }
    }

    override fun onTrainingClick(training: Training?, date: LocalDate) {
        if (training == null) {
            val action =
                HomeScreenFragmentDirections.actionHomeScreenFragmentToNewTrainingVariantsFragment(
                    trainingDate = date
                )
            findNavController().navigate(action)
        } else {
            val action =
                HomeScreenFragmentDirections
                    .actionHomeScreenFragmentToAddEditTrainingScreenFragment(
                        trainingDate = training.trainingDate,
                        trainingId = training.trainingId,
                        trainingName = training.trainingName,
                        isFromFavourites = false
                    )
            findNavController().navigate(action)
        }
    }


    override fun onFabClicked() {
        val action =
            HomeScreenFragmentDirections.actionHomeScreenFragmentToNewTrainingVariantsFragment(
                trainingDate = LocalDate.now()
            )
        findNavController().navigate(action)
    }

}