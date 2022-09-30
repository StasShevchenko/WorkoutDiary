package com.example.workoutdiary.presentation.add_edit_training_block_screen

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.workoutdiary.R
import com.example.workoutdiary.databinding.AddEditTrainingBlockScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddEditTrainingBlockScreenFragment : Fragment(R.layout.add_edit_training_block_screen) {
    private val viewModel: AddEditTrainingBlockScreenViewModel by viewModels()
    private lateinit var binding: AddEditTrainingBlockScreenBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddEditTrainingBlockScreenBinding.bind(view)
        binding.scrollView2.visibility = View.INVISIBLE

        // binding.scrollView2.visibility = View.INVISIBLE
        binding.apply {
            if (viewModel.currentTrainingBlockId != 0) {
                deleteTrainingBlockButton.visibility = View.VISIBLE
            }

            deleteTrainingBlockButton.setOnClickListener {
                viewModel.onEvent(AddEditTrainingBlockScreenEvent.DeleteChosen)
            }
            numberPickerDecreaseButton.setOnClickListener {
                viewModel.onEvent(AddEditTrainingBlockScreenEvent.SetNumberDecreased)
            }
            numberPickerIncreaseButton.setOnClickListener {
                viewModel.onEvent(AddEditTrainingBlockScreenEvent.SetNumberIncreased)
            }
            saveTrainingBlockButton.setOnClickListener{
                viewModel.onEvent(AddEditTrainingBlockScreenEvent.SaveChosen)
            }
            setsListView.apply {
                setOnRepsEntered { index, value ->
                    viewModel.onEvent(AddEditTrainingBlockScreenEvent.RepsEntered(index, value))
                }
                setOnWeightEntered{ index, value ->
                    viewModel.onEvent(AddEditTrainingBlockScreenEvent.WeightEntered(index, value))
                }
            }


            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                    viewModel.currentExercise.collectLatest { exercise ->
                        viewModel.validateSets.collectLatest { parametersList ->
                            binding.numberTextView.text = parametersList.size.toString()
                            if(parametersList.isNotEmpty()) {
                                binding.setsListView.submitSetsList(
                                    parametersList,
                                    exercise?.exerciseType ?: "NOT SELECTED"
                                )
                            }
                            binding.scrollView2.visibility = View.VISIBLE
                            binding.progressBar.visibility = View.INVISIBLE
                        }
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.muscles.collect { muscles ->
                        viewModel.currentMuscle.value?.let { currentMuscle ->
                            binding.muscleChoiceView.setText(currentMuscle.muscleName)
                        }
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

            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.eventFlow.collectLatest { event ->
                    when (event) {
                        is AddEditTrainingBlockScreenViewModel.UiEvent.SavePressed -> {
                            findNavController().popBackStack()
                        }
                        AddEditTrainingBlockScreenViewModel.UiEvent.DeletePressed -> {
                            findNavController().popBackStack()
                        }
                    }
                }
            }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.currentExercise.collectLatest { currentExercise ->
                        if (currentExercise == null) {
                            binding.exerciseChoiceView.setText("")
                        } else {
                            binding.exerciseChoiceView.setText(currentExercise.exerciseName)
                            val exercisesAdapter = ArrayAdapter(
                                requireContext(),
                                R.layout.muscle_exercise_drop_down_item,
                                viewModel.exercise.value
                            )
                            binding.exerciseChoiceView.setAdapter(exercisesAdapter)
                        }
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
                            viewModel.onEvent(
                                AddEditTrainingBlockScreenEvent.ExerciseSelected(
                                    exercises[i]
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
