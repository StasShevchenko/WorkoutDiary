package com.example.workoutdiary.presentation.add_edit_training_block_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.workoutdiary.R
import com.example.workoutdiary.databinding.AddEditTrainingBlockScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddEditTrainingBlockScreenFragment : Fragment(R.layout.add_edit_training_block_screen) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: AddEditTrainingBlockScreenViewModel by viewModels()

        val binding = AddEditTrainingBlockScreenBinding.bind(view)
        binding.apply {
            numberPickerDecreaseButton.setOnClickListener {
                viewModel.onEvent(AddEditTrainingBlockScreenEvent.SetNumberDecreased)
            }
            numberPickerIncreaseButton.setOnClickListener {
                viewModel.onEvent(AddEditTrainingBlockScreenEvent.SetNumberIncreased)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.muscles.collect { muscles ->
                    val muscleAdapter =
                        ArrayAdapter(
                            requireContext(),
                            R.layout.muscle_exercise_drop_down_item,
                            muscles
                        )
                    binding.muscleChoiceView.setAdapter(muscleAdapter)
                    binding.muscleChoiceView.setOnItemClickListener { _, _, i, _ ->
                        viewModel.onEvent(AddEditTrainingBlockScreenEvent.MuscleSelected(muscles[i]))
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentExercise.collectLatest { exercise ->
                    when (exercise?.exerciseType) {
                        "Вес и повторения" -> {
                            for (i: Int in 1..viewModel.setCounter.value) {
                                binding.setsLayout.addView(
                                    LayoutInflater.from(requireContext()).inflate(
                                        R.layout.rep_weght_training_set,
                                        binding.setsLayout,
                                        false
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.setCounter.collectLatest { setCount ->
                    binding.numberTextView.text = setCount.toString()
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.exercise.collect { exercises ->
                    val exercisesAdapter = ArrayAdapter(
                        requireContext(),
                        R.layout.muscle_exercise_drop_down_item,
                        exercises
                    )
                    binding.exerciseChoiceView.setAdapter(exercisesAdapter)
                    binding.exerciseChoiceView.setOnItemClickListener { _, _, i, _ ->
                        viewModel.onEvent(AddEditTrainingBlockScreenEvent.ExerciseSelected(exercises[i]))
                    }
                }
            }
        }
    }
}
