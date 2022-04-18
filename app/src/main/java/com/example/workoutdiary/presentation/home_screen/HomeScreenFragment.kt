package com.example.workoutdiary.presentation.home_screen

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutdiary.R
import com.example.workoutdiary.databinding.HomeScreenFragmentBinding

import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate

@AndroidEntryPoint
class HomeScreenFragment : Fragment(R.layout.home_screen_fragment) {
    private val viewModel: HomeScreenViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = HomeScreenFragmentBinding.bind(view)

        val trainingDaysAdapter = TrainingDaysAdapter()

        binding.apply {
            trainingRecyclerView.apply {
                adapter = trainingDaysAdapter
                layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
            }
            trainingDaysAdapter.submitList(viewModel.trainingDaysList.value)
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
}