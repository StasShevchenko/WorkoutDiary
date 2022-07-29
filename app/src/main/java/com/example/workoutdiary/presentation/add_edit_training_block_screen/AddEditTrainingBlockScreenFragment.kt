package com.example.workoutdiary.presentation.add_edit_training_block_screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.children
import androidx.core.view.forEach
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
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class AddEditTrainingBlockScreenFragment : Fragment(R.layout.add_edit_training_block_screen) {
    val viewModel: AddEditTrainingBlockScreenViewModel by viewModels()
    lateinit var binding: AddEditTrainingBlockScreenBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddEditTrainingBlockScreenBinding.bind(view)
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
                                if (currentView.id == R.id.rep_weight_item) {
                                    val repsEditText =
                                        currentView.findViewById<TextInputEditText>(R.id.repweight_reps_edit_text)
                                    val weightEditText =
                                        currentView.findViewById<TextInputEditText>(R.id.repweight_weight_edit_text)
                                    try {
                                        val currentReps = repsEditText.text.toString().toInt()
                                        if (currentReps == 0) {
                                            repsEditText.error = "Введите данные"
                                            saveFlag = false
                                        }
                                    } catch (e: NumberFormatException) {
                                        repsEditText.error = "Введите данные"
                                        saveFlag = false
                                    }
                                    try {
                                        val currentWeight = weightEditText.text.toString().toInt()
                                        if (currentWeight == 0) {
                                            weightEditText.error = "Введите данные"
                                            saveFlag = false
                                        }
                                    } catch (e: NumberFormatException) {
                                        weightEditText.error = "Введите данные"
                                        saveFlag = false
                                    }
                                }
                            }
                            if (saveFlag) {
                                viewModel.onEvent(AddEditTrainingBlockScreenEvent.SaveChosen)
                            }
                        }
                        "REPS" -> {
                            var saveFlag = true
                            binding.setsLayout.children.forEach { currentView ->
                                try {
                                    if (currentView.id == R.id.rep_item) {
                                        val currentReps =
                                            currentView.findViewById<TextInputEditText>(R.id.rep_reps_edit_text).text.toString()
                                                .toInt()
                                        if (currentReps == 0) {
                                            currentView.findViewById<TextInputEditText>(R.id.rep_reps_edit_text)
                                                .setText("Введите данные")
                                            saveFlag = false
                                        }
                                    }
                                } catch (e: NumberFormatException) {
                                    currentView.findViewById<TextInputEditText>(R.id.rep_reps_edit_text).error =
                                        "Введите данные"
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
                }
            }
        }

        //Adding list items
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.currentExercise.collectLatest { exercise ->
                    viewModel.setCounter.collectLatest { setsNumber ->
                        when (exercise?.exerciseType) {
                            "WEIGHT AND REPS" -> {
                                if (viewModel.previousExercise == null || viewModel.previousExercise?.exerciseType != "WEIGHT AND REPS") {
                                    binding.setsLayout.removeAllViews()
                                    addRepWeightItems(setsNumber)
                                    restoreRepWeightItemsData(viewModel.parameterizedSets.value)
                                    viewModel.onEvent(
                                        AddEditTrainingBlockScreenEvent.SetsRendered(
                                            exercise
                                        )
                                    )
                                } else {
                                    if (setsNumber > (binding.setsLayout.childCount + 1) / 2) {
                                        addRepWeightItems(setsNumber - (binding.setsLayout.childCount + 1) / 2)
                                    } else if (setsNumber < (binding.setsLayout.childCount + 1) / 2) {
                                        binding.setsLayout.removeViewAt(binding.setsLayout.childCount - 1)
                                        binding.setsLayout.removeViewAt(binding.setsLayout.childCount - 1)
                                    }
                                }
                            }
                            "REPS" -> {
                                if (viewModel.previousExercise == null || viewModel.previousExercise?.exerciseType != "REPS") {
                                    binding.setsLayout.removeAllViews()
                                    addRepItems(setsNumber)
                                    restoreRepItemsData(viewModel.parameterizedSets.value)
                                    viewModel.onEvent(
                                        AddEditTrainingBlockScreenEvent.SetsRendered(
                                            exercise
                                        )
                                    )
                                } else {
                                    if (setsNumber > (binding.setsLayout.childCount) / 2) {
                                        addRepItems(setsNumber - (binding.setsLayout.childCount) / 2)
                                    } else {
                                        if (setsNumber < (binding.setsLayout.childCount) / 2) {
                                            binding.setsLayout.removeViewAt(binding.setsLayout.childCount - 1)
                                            binding.setsLayout.removeViewAt(binding.setsLayout.childCount - 1)
                                        }
                                    }
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
                            restoreRepWeightItemsData(setParameters)
                        }
                        "REPS" -> {
                            restoreRepItemsData(setParameters)
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.currentExercise.collectLatest { currentExercise ->
                    if (currentExercise == null) {
                        binding.setsLayout.removeAllViews()
                        binding.exerciseChoiceView.setText("")
                    } else{
                        binding.exerciseChoiceView.setText(currentExercise.exerciseName)
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.currentMuscle.collectLatest { currentMuscle ->
                    currentMuscle?.let {
                       // binding.muscleChoiceView.setText(currentMuscle.muscleName)
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

    private fun restoreRepWeightItemsData(setParameters: MutableList<ParameterizedSet>) {
        var counter = 0
        binding.setsLayout.children.forEach {
            if (it.id == R.id.rep_weight_item) {
                it.findViewById<TextInputEditText>(R.id.repweight_reps_edit_text)
                    .setText(if (setParameters[counter].repeats == null) "" else setParameters[counter].repeats.toString())
                it.findViewById<TextInputEditText>(R.id.repweight_weight_edit_text)
                    .setText(if (setParameters[counter].weight == null) "" else setParameters[counter].weight.toString())
                counter++
            }
        }
    }

    private fun restoreRepItemsData(setParameters: MutableList<ParameterizedSet>){
        var counter = 0
        binding.setsLayout.children.forEach {
            if (it.id == R.id.rep_item) {
                it.findViewById<TextInputEditText>(R.id.rep_reps_edit_text)
                    .setText(
                        if (setParameters[counter].repeats == null) ""
                        else setParameters[counter].repeats.toString()
                    )
                counter++
            }
        }
    }

    private fun addRepItems(amount: Int) {
        for (i in 1..amount) {
            val setNumberTextView = TextView(requireContext())
            setNumberTextView.text =
                "Подход ${(binding.setsLayout.childCount / 2) + 1}"
            setNumberTextView.setTextAppearance(androidx.transition.R.style.TextAppearance_AppCompat_Body1)
            binding.setsLayout.addView(setNumberTextView)
            (setNumberTextView.layoutParams as LinearLayout.LayoutParams).leftMargin =
                resources.getDimension(R.dimen.mediumDimension)
                    .toInt()
            val repItemBinding =
                LayoutInflater.from(requireContext()).inflate(
                    R.layout.rep_training_block_item,
                    binding.setsLayout,
                    false
                )

            binding.setsLayout.addView(repItemBinding)

            val index = (binding.setsLayout.childCount / 2) - 1
            repItemBinding.findViewById<TextInputEditText>(R.id.rep_reps_edit_text)
                .addTextChangedListener { text ->
                    viewModel.onEvent(
                        AddEditTrainingBlockScreenEvent.RepsEntered(
                            index,
                            text.toString()
                        )
                    )
                }
        }
    }


    private fun addRepWeightItems(amount: Int) {
        for (i in 1..amount) {
            val setNumberTextView = TextView(requireContext())
            setNumberTextView.text =
                "Подход ${(binding.setsLayout.childCount / 2) + 1}"
            setNumberTextView.setTextAppearance(androidx.transition.R.style.TextAppearance_AppCompat_Body1)
            binding.setsLayout.addView(setNumberTextView)
            (setNumberTextView.layoutParams as LinearLayout.LayoutParams).leftMargin =
                resources.getDimension(R.dimen.mediumDimension)
                    .toInt()
            val repWeightItemBinding =
                LayoutInflater.from(requireContext()).inflate(
                    R.layout.rep_weght_training_block_item,
                    binding.setsLayout,
                    false
                )

            binding.setsLayout.addView(repWeightItemBinding)

            val index = (binding.setsLayout.childCount / 2) - 1
            repWeightItemBinding.findViewById<TextInputEditText>(R.id.repweight_reps_edit_text)
                .addTextChangedListener { text ->
                    viewModel.onEvent(
                        AddEditTrainingBlockScreenEvent.RepsEntered(
                            index,
                            text.toString()
                        )
                    )
                }
            repWeightItemBinding.findViewById<TextInputEditText>(R.id.repweight_weight_edit_text)
                .addTextChangedListener { text ->
                    viewModel.onEvent(
                        AddEditTrainingBlockScreenEvent.WeightEntered(
                            index,
                            text.toString()
                        )
                    )
                }
        }
    }
}
