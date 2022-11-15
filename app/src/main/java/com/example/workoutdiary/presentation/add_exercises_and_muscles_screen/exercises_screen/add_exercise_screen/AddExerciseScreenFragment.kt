package com.example.workoutdiary.presentation.add_exercises_and_muscles_screen.exercises_screen.add_exercise_screen

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.workoutdiary.R
import com.example.workoutdiary.databinding.AddExerciseScreenFragmentBinding
import com.example.workoutdiary.presentation.add_edit_training_screen.AddEditTrainingScreenEvent
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.Duration

@AndroidEntryPoint
class AddExerciseScreenFragment : Fragment(R.layout.add_exercise_screen_fragment) {
    private val viewModel: AddExerciseScreenViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = AddExerciseScreenFragmentBinding.bind(requireView())
        val exerciseMapper = ExerciseTypeMapper(resources.getStringArray(R.array.exercise_types))
        if (arguments?.getInt("exerciseId") != null) {
            binding.addExerciseButton.text = "обновить упражнение"
            binding.headerTextView.text = "Обновление упражнения"
        }
        binding.apply {
            exerciseTypeChoiceView.setText(resources.getStringArray(R.array.exercise_types)[0])
            val exerciseTypeAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                resources.getStringArray(R.array.exercise_types)
            )
            exerciseTypeChoiceView.setAdapter(exerciseTypeAdapter)
            exerciseNameEditText.addTextChangedListener { text ->
                viewModel.onEvent(AddExercisesScreenEvent.ExerciseNameEntered(text.toString()))
            }
            exerciseTypeChoiceView.setOnItemClickListener { _, _, i, _ ->
                viewModel.onEvent(
                    AddExercisesScreenEvent.ExerciseTypeSelected(
                        exerciseMapper.getValueByPosition(
                            i
                        )
                    )
                )
            }
            exerciseDescriptionEditText.addTextChangedListener { text ->
                viewModel.onEvent(AddExercisesScreenEvent.ExerciseDescriptionEntered(text.toString()))
            }
            addExerciseButton.setOnClickListener {
                viewModel.onEvent(AddExercisesScreenEvent.ExerciseAdded)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentExercise.collectLatest { exercise ->
                    exercise?.let { exercise ->
                        binding.apply {
                            exerciseNameEditText.setText(exercise.exerciseName)
                            exerciseDescriptionEditText.setText(exercise.exerciseDescription)
                            exerciseTypeChoiceView.setText(exerciseMapper.getNameByValue(exercise.exerciseType))
                            val exerciseTypeAdapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_list_item_1,
                                resources.getStringArray(R.array.exercise_types)
                            )
                            exerciseTypeChoiceView.setAdapter(exerciseTypeAdapter)
                            muscleGroupChoiceView.setText(viewModel.currentMuscle!!.muscleName)
                        }
                    }
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.muscles.collectLatest { muscles ->
                    val musclesAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        muscles
                    )
                    binding.muscleGroupChoiceView.setAdapter(musclesAdapter)
                    binding.muscleGroupChoiceView.setOnItemClickListener { _, _, i, _ ->
                        viewModel.onEvent(AddExercisesScreenEvent.MuscleSelected(muscles[i]))
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collectLatest { uiEvent ->
                when (uiEvent) {
                    AddExerciseScreenViewModel.UiEvent.AddPressed -> {
                        findNavController().popBackStack()
                    }
                    AddExerciseScreenViewModel.UiEvent.UpdatePressed -> {
                        AlertDialog.Builder(requireContext())
                            .setTitle("Обновление упражнения")
                            .setMessage("Внимание! Данные об упражнении будут обновлены и во всех тренировках. Продолжить?")
                            .setPositiveButton("Да") { _, _ ->
                                viewModel.onEvent(AddExercisesScreenEvent.UpdateConfirmed)
                            }.create()
                            .show()
                    }
                    AddExerciseScreenViewModel.UiEvent.ExerciseAlreadyExists -> {
                        Snackbar.make(
                            view,
                            "Такое упражнение уже существует!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    AddExerciseScreenViewModel.UiEvent.ValidationFailed -> {
                        Snackbar.make(
                            view,
                            "Заполните все необходимые поля!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


}