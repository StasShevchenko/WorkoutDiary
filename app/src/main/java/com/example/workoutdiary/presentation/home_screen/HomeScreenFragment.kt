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
import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.databinding.HomeScreenFragmentBinding
import com.example.workoutdiary.presentation.MainActivity
import com.example.workoutdiary.presentation.utils.FabButtonClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class HomeScreenFragment : Fragment(R.layout.home_screen_fragment), FabButtonClick, TrainingDaysAdapter.OnTrainingClickListener {
    private val trainingsViewModel: TrainingsViewModel by viewModels()
    private val statisticsViewModel: StatisticsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as MainActivity
        activity.setFabListener(this)
        val binding = HomeScreenFragmentBinding.bind(view)

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