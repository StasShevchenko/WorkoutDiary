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
import com.example.workoutdiary.presentation.utils.FabButtonClick
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.horizontal.HorizontalAxis
import com.patrykandpatrick.vico.core.chart.insets.Insets
import com.patrykandpatrick.vico.core.chart.segment.SegmentProperties
import com.patrykandpatrick.vico.core.component.OverlayingComponent
import com.patrykandpatrick.vico.core.component.marker.MarkerComponent
import com.patrykandpatrick.vico.core.component.shape.DashedShape
import com.patrykandpatrick.vico.core.component.shape.LineComponent
import com.patrykandpatrick.vico.core.component.shape.ShapeComponent
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.cornered.Corner
import com.patrykandpatrick.vico.core.component.shape.cornered.MarkerCorneredShape
import com.patrykandpatrick.vico.core.component.text.textComponent
import com.patrykandpatrick.vico.core.context.MeasureContext
import com.patrykandpatrick.vico.core.extension.copyColor
import com.patrykandpatrick.vico.views.dimensions.dimensionsOf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class HomeScreenFragment : Fragment(R.layout.home_screen_fragment), FabButtonClick, TrainingDaysAdapter.OnTrainingClickListener {
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
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }

            monthDatePicker.setOnDateChangedListener {
                trainingsViewModel.onEvent(HomeScreenEvent.ChangeDate(it))
            }

            monthDatePicker.setOnTextClickedAction {
                trainingRecyclerView.smoothScrollToPosition(LocalDate.now().dayOfMonth-1)
            }

            emptyParametersMessageTextView.setOnClickListener {
                val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToEditStatisticsScreenFragment()
                findNavController().navigate(action)
            }

            emptyDataMessageTextView.setOnClickListener {
                val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToEditStatisticsScreenFragment()
                findNavController().navigate(action)
            }

            openStatisticsParametersButton.setOnClickListener {
                val action = HomeScreenFragmentDirections.actionHomeScreenFragmentToEditStatisticsScreenFragment()
                findNavController().navigate(action)
            }


            with(chart) {
                entryProducer = statisticsViewModel.statisticsEntryProducer
                (bottomAxis as HorizontalAxis).valueFormatter =
                    AxisValueFormatter { value, chartValues ->
                        if(chartValues.chartEntryModel.entries.isNotEmpty()){
                            (chartValues.chartEntryModel.entries[0].getOrNull(value.toInt()) as DateEntry?)
                                ?.date
                                ?.run { this }
                                .orEmpty()
                        }
                        else "There is no any data"
                    }
                val labelBackgroundShape = MarkerCorneredShape(all = Corner.FullyRounded)
                val label = textComponent {
                    this.color = -1
                    this.padding = dimensionsOf(horizontalDp = 8f, verticalDp = 4f)
                    this.background = ShapeComponent(
                        shape = labelBackgroundShape,
                        color = -1
                    )
                        .setShadow(
                            radius = 4f,
                            dy = 2f,
                            applyElevationOverlay = true
                        )
                }
                val indicatorInner =
                    ShapeComponent(shape = Shapes.pillShape, color = R.color.purple_500)
                val indicatorCenter = ShapeComponent(shape = Shapes.pillShape, color = R.color.white)
                val indicatorOuter = ShapeComponent(shape = Shapes.pillShape, color = R.color.white)

                val indicator = OverlayingComponent(
                    outer = indicatorOuter,
                    innerPaddingAllDp = 10f,
                    inner = OverlayingComponent(
                        outer = indicatorCenter,
                        inner = indicatorInner,
                        innerPaddingAllDp = 5f
                    ),
                )
                val guideline = LineComponent(
                    color = R.color.white,
                    thicknessDp = 2f,
                    shape = DashedShape(
                        shape = Shapes.pillShape,
                        dashLengthDp = 8f,
                        gapLengthDp = 4f
                    )
                )
                this.marker = object : MarkerComponent(
                    label = label,
                    indicator = indicator,
                    guideline = guideline
                ) {
                    init {
                        indicatorSizeDp = 30f
                        onApplyEntryColor = {entryColor ->
                            indicatorOuter.color = entryColor.copyColor(alpha = 32)
                            with(indicatorCenter) {
                                color = entryColor
                                setShadow(radius = 12f, color = entryColor)
                            }
                        }
                    }
                    override fun getInsets(
                        context: MeasureContext,
                        outInsets: Insets,
                        segmentProperties: SegmentProperties
                    ) = with(context) {
                        outInsets.top = label.getHeight(this) + labelBackgroundShape.tickSizeDp.pixels +
                                4f.pixels * 1.3f - 2f.pixels
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch{
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                    trainingsViewModel.trainingDaysList
                        .collectLatest {
                            trainingDaysAdapter.submitList(it)
                        }
                }
               launch {
                   statisticsViewModel.totalTrainingsCount.collectLatest { totalTrainingsCount ->
                       binding.totalTrainingsCountTextView.text = "Всего тренировок: ${totalTrainingsCount}"
                   }
               }
                launch {
                    statisticsViewModel.totalSetsCount.collectLatest { totalSetsCount ->
                        binding.totalSetsCountTextView.text = "Всего выполнено подходов: ${totalSetsCount}"
                    }
                }
                launch {
                    statisticsViewModel.totalRepsCount.collectLatest { totalRepsCount ->
                        binding.totalRepsCountTextView.text = "Всего повторений в упражнениях: ${totalRepsCount}"
                    }
                }
                launch {
                    statisticsViewModel.totalWeightCount.collectLatest { totalWeightCount ->
                        binding.totalWeightCountTextView.text = "Всего поднято: ${totalWeightCount} кг веса"
                    }
                }
                launch {
                    statisticsViewModel.statisticsInfo.collectLatest { statisticsValue ->
                        statisticsValue?.let {
                            binding.exerciseNameTextView.text = statisticsValue.first
                            updateStaticsVisibility(statisticsValue.second, statisticsViewModel.statisticsParameters.value)
                        }
                    }
                }
                launch {
                    statisticsViewModel.statisticsParameters.collectLatest { parameters ->
                        updateStaticsVisibility(statisticsViewModel.statisticsInfo.value?.second, parameters)
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
            }
        } else if (statisticsData == null || statisticsData.isEmpty()) {
            with(binding){
                statisticsCardView.visibility = View.VISIBLE
                emptyParametersMessageTextView.visibility = View.GONE
                emptyDataMessageTextView.visibility = View.VISIBLE
            }
        } else {
            with(binding){
                statisticsCardView.visibility = View.VISIBLE
                emptyParametersMessageTextView.visibility = View.GONE
                emptyDataMessageTextView.visibility = View.GONE
                statisticsChartContainer.visibility = View.VISIBLE
            }
        }
    }

    override fun onTrainingClick(training: Training?, date: LocalDate) {
        val action =
            HomeScreenFragmentDirections
                .actionHomeScreenFragmentToAddEditTrainingScreenFragment(
                    trainingDate = training?.trainingDate ?: date,
                    trainingId = training?.trainingId ?: -1,
                    trainingName = training?.trainingName ?: ""
                )
        findNavController().navigate(action)
    }


    override fun onFabClicked() {
        val action =
            HomeScreenFragmentDirections
                .actionHomeScreenFragmentToAddEditTrainingScreenFragment(
                    trainingDate = LocalDate.now(),
                    trainingId = trainingsViewModel.todayTraining?.trainingId ?: -1,
                    trainingName = trainingsViewModel.todayTraining?.trainingName ?: ""
                )
        findNavController().navigate(action)
    }
}