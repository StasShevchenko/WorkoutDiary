package com.example.workoutdiary.presentation.edit_statistics_screen

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
import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.databinding.EditStatisticsScreenBinding
import com.example.workoutdiary.utils.ExerciseType
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EditStatisticsScreenFragment : Fragment(R.layout.edit_statistics_screen) {
    private val viewModel: EditStatisticsScreenViewModel by viewModels()
    private lateinit var binding: EditStatisticsScreenBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = EditStatisticsScreenBinding.bind(view)
        showRadioButtonsByExerciseType(null)
        with(binding) {
            saveButton.setOnClickListener {
                viewModel.savePressed()
            }
            resetButton.setOnClickListener {
                viewModel.resetCurrentParameters()
            }
            repsRadioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.statisticsParameter = "repeats"
                }
            }
            weightRadioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.statisticsParameter = "weight"
                }
            }
            timeRadioButton.setOnCheckedChangeListener { _, isChecked ->
                if(isChecked){
                    viewModel.statisticsParameter = "time"
                }
            }
            distanceRadioButton.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.statisticsParameter = "distance"
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.exercises.collectLatest { exercises ->
                        val exercisesAdapter = ArrayAdapter(
                            requireContext(),
                            R.layout.muscle_exercise_drop_down_item,
                            exercises
                        )
                        binding.exerciseChoiceView.setAdapter(exercisesAdapter)
                        binding.exerciseChoiceView.setOnItemClickListener { parent, _, i, _ ->
                            viewModel.exerciseSelected(parent.getItemAtPosition(i) as Exercise)
                        }
                    }
                }
                launch {
                    viewModel.currentExercise.collectLatest { exercise ->
                        exercise?.let {
                            showRadioButtonsByExerciseType(exercise.exerciseType)
                            binding.exerciseChoiceView.setText(exercise.exerciseName)
                        }
                    }
                }
                launch {
                    viewModel.currentExerciseStatisticsParameters.collectLatest {parameters ->
                        if(parameters != null){
                            binding.resetButton.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    EditStatisticsScreenViewModel.UiEvent.SavePressed -> {
                        findNavController().popBackStack()
                    }
                    EditStatisticsScreenViewModel.UiEvent.ValidationFailed -> {
                        val snackbar = Snackbar.make(
                            view,
                            getString(R.string.choose_an_exercise),
                            Snackbar.LENGTH_SHORT
                        )
                        snackbar.setAction("OK") {
                            snackbar.dismiss()
                        }
                        snackbar.show()
                    }
                }
            }
        }

    }

    private fun showRadioButtonsByExerciseType(exerciseType: ExerciseType?) {
        when (exerciseType) {
            ExerciseType.REPS -> {
                with(binding) {
                    radioGroup.clearCheck()
                    repsRadioButton.visibility = View.VISIBLE
                    repsRadioButton.isChecked = true
                    weightRadioButton.visibility = View.GONE
                    timeRadioButton.visibility = View.GONE
                    distanceRadioButton.visibility = View.GONE
                }
            }
            ExerciseType.WEIGHT_AND_REPS -> {
                with(binding) {
                    radioGroup.clearCheck()
                    repsRadioButton.visibility = View.VISIBLE
                    if (viewModel.currentExerciseStatisticsParameters.value != null) {
                        if (viewModel.currentExerciseStatisticsParameters.value!!.statisticsParameter == "weight") {
                            weightRadioButton.isChecked = true
                        } else{
                            repsRadioButton.isChecked = true
                        }
                    }else{
                        repsRadioButton.isChecked = true
                    }
                    weightRadioButton.visibility = View.VISIBLE
                    timeRadioButton.visibility = View.GONE
                    distanceRadioButton.visibility = View.GONE
                }
            }
            ExerciseType.TIME -> {
                with(binding) {
                    radioGroup.clearCheck()
                    repsRadioButton.visibility = View.GONE
                    weightRadioButton.visibility = View.GONE
                    timeRadioButton.visibility = View.VISIBLE
                    timeRadioButton.isChecked = true
                    distanceRadioButton.visibility = View.GONE
                }
            }
            ExerciseType.DISTANCE -> {
                with(binding) {
                    radioGroup.clearCheck()
                    repsRadioButton.visibility = View.GONE
                    weightRadioButton.visibility = View.GONE
                    timeRadioButton.visibility = View.GONE
                    distanceRadioButton.visibility = View.VISIBLE
                    distanceRadioButton.isChecked = true
                }
            }
            null -> {
                with(binding) {
                    radioGroup.clearCheck()
                    repsRadioButton.visibility = View.GONE
                    weightRadioButton.visibility = View.GONE
                    timeRadioButton.visibility = View.GONE
                    distanceRadioButton.visibility = View.GONE
                }
            }
        }
    }

}