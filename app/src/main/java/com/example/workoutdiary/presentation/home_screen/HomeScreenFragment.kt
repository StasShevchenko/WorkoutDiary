package com.example.workoutdiary.presentation.home_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutdiary.R
import com.example.workoutdiary.databinding.HomeScreenFragmentBinding
import com.example.workoutdiary.presentation.MainActivity
import com.example.workoutdiary.presentation.utils.FabButtonClick
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class HomeScreenFragment : Fragment(R.layout.home_screen_fragment), FabButtonClick {
    private val viewModel: HomeScreenViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as MainActivity
        activity.setFabListener(this)
        val binding = HomeScreenFragmentBinding.bind(view)

        val trainingDaysAdapter = TrainingDaysAdapter()

        binding.apply {
            trainingRecyclerView.apply {
                adapter = trainingDaysAdapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }

            monthDatePicker.setOnDateChangedListener {
                viewModel.onEvent(HomeScreenEvent.ChangeDate(it))
            }

            monthDatePicker.setOnTextClickedAction {
                trainingRecyclerView.smoothScrollToPosition(LocalDate.now().dayOfMonth-1)
            }

        }
        viewModel.trainingDaysList.observe(viewLifecycleOwner){
        trainingDaysAdapter.submitList(it)
        }

    }

    override fun onFabClicked() {
        val action =
            HomeScreenFragmentDirections
                .actionHomeScreenFragmentToAddEditTrainingScreenFragment(trainingDate = LocalDate.now(), trainingId = -1)
        findNavController().navigate(action)
    }
}