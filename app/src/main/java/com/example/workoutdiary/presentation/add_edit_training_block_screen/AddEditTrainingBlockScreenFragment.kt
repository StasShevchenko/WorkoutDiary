package com.example.workoutdiary.presentation.add_edit_training_block_screen

import android.app.ActionBar
import android.os.Bundle
import android.text.Editable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.core.view.allViews
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.workoutdiary.R
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import com.example.workoutdiary.databinding.AddEditTrainingBlockScreenBinding
import com.example.workoutdiary.presentation.add_edit_training_screen.AddEditTrainingScreenViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.lang.NumberFormatException


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
            saveTrainingBlockButton.setOnClickListener {
                if (viewModel.currentExercise.value != null) {
                    when (viewModel.currentExercise.value!!.exerciseType) {
                        "WEIGHT AND REPS" -> {
                            var saveFlag = true
                            binding.setsLayout.children.forEach { currentView ->
                                try {
                                    if (currentView.id == R.id.reps_edit_text) {
                                        val currentReps =
                                            (currentView as TextInputEditText).text.toString()
                                                .toInt()
                                        if (currentReps == 0) {
                                            currentView.error =
                                                "Введите данные"
                                            saveFlag = false
                                        }
                                    }
                                    if (currentView.id == R.id.weight_edit_text) {
                                        val currentWeight =
                                            (currentView as TextInputEditText).text.toString()
                                                .toInt()
                                        if (currentWeight == 0) {
                                            currentView.error =
                                                "Введите данные"
                                            saveFlag = false
                                        }
                                    }
                                } catch (e: NumberFormatException) {
                                    (currentView as TextInputEditText).error = "Введите данные"
                                    saveFlag = false
                                }
                            }
                            if (saveFlag) {
                                viewModel.onEvent(AddEditTrainingBlockScreenEvent.SaveChosen)
                            }
                        }
                    }
                }
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

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is AddEditTrainingBlockScreenViewModel.UiEvent.SavePressed -> {
                        findNavController().popBackStack()
                    }
                }
            }
        }



        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.currentExercise.collectLatest { exercise ->
                    viewModel.setCounter.collectLatest { setsNumber ->
                        when (exercise?.exerciseType) {
                            "WEIGHT AND REPS" -> {
                                if (setsNumber > (binding.setsLayout.childCount + 1) / 2) {
                                    for (i in 1..(setsNumber - (binding.setsLayout.childCount + 1) / 2)) {
                                        val setNumberTextView = TextView(requireContext())
                                        setNumberTextView.text =
                                            "Подход ${(binding.setsLayout.childCount / 2) + 1}"
                                        setNumberTextView.setTextAppearance(androidx.transition.R.style.TextAppearance_AppCompat_Body1)
                                        binding.setsLayout.addView(setNumberTextView)
                                        (setNumberTextView.layoutParams as LinearLayout.LayoutParams).leftMargin =
                                            resources.getDimension(R.dimen.mediumDimension).toInt()
                                        val repWeightItemBinding =
                                            LayoutInflater.from(requireContext()).inflate(
                                                R.layout.rep_weght_training_set,
                                                binding.setsLayout,
                                                false
                                            )

                                        binding.setsLayout.addView(repWeightItemBinding)

                                        val index = (binding.setsLayout.childCount / 2) - 1
                                        repWeightItemBinding.findViewById<TextInputEditText>(R.id.reps_edit_text)
                                            .addTextChangedListener { text ->
                                                viewModel.onEvent(
                                                    AddEditTrainingBlockScreenEvent.RepsEntered(
                                                        index,
                                                        text.toString()
                                                    )
                                                )
                                            }
                                        repWeightItemBinding.findViewById<TextInputEditText>(R.id.weight_edit_text)
                                            .addTextChangedListener { text ->
                                                viewModel.onEvent(
                                                    AddEditTrainingBlockScreenEvent.WeightEntered(
                                                        index,
                                                        text.toString()
                                                    )
                                                )
                                            }
                                    }

                                } else if (setsNumber < (binding.setsLayout.childCount + 1) / 2) {
                                    binding.setsLayout.removeViewAt(binding.setsLayout.childCount - 1)
                                    binding.setsLayout.removeViewAt(binding.setsLayout.childCount - 1)
                                }
                            }
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.parameterizedSets.collectLatest { setParameters ->
                    when (viewModel.currentExercise.value?.exerciseType) {
                        "WEIGHT AND REPS" -> {
                            var counter = 0
                            binding.setsLayout.children.forEach {
                                if (it.id == R.id.rep_weight_item) {
                                    it.findViewById<TextInputEditText>(R.id.reps_edit_text)
                                        .setText(if (setParameters[counter].repeats == null) "" else setParameters[counter].repeats.toString())
                                    it.findViewById<TextInputEditText>(R.id.weight_edit_text)
                                        .setText(if (setParameters[counter].weight == null) "" else setParameters[counter].weight.toString())
                                    counter++
                                }
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
