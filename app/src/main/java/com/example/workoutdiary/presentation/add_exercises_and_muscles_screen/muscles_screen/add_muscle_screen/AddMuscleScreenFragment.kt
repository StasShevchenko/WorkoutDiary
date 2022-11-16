package com.example.workoutdiary.presentation.add_exercises_and_muscles_screen.muscles_screen.add_muscle_screen

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.workoutdiary.R
import com.example.workoutdiary.databinding.AddMuscleScreenFragmentBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddMuscleScreenFragment : Fragment(R.layout.add_muscle_screen_fragment) {
    private val viewModel: AddMuscleScreenViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = AddMuscleScreenFragmentBinding.bind(view)
        if (arguments?.getInt("muscleId") != null) {
            binding.headerTextView.text = "Обновление мышечной группы"
            binding.addMuscleButton.text = "Обновить мышечную группу"
        }

        binding.apply {
            muscleNameEditText.addTextChangedListener { text ->
                viewModel.updateMuscleName(text.toString().trim())
            }
            addMuscleButton.setOnClickListener {
                viewModel.addMuscle()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.muscleNameError.collectLatest { error ->
                    if (error) {
                        binding.muscleNameEditText.error = "Введите название мышечной группы"
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentMuscle.collectLatest { muscle ->
                    muscle?.let {
                        binding.muscleNameEditText.setText(muscle.muscleName)
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    AddMuscleScreenViewModel.UiEvent.MuscleAdded -> {
                        findNavController().popBackStack()
                    }
                    AddMuscleScreenViewModel.UiEvent.MuscleAlreadyExists -> {
                        Snackbar.make(
                            requireView(),
                            "Мышечная группа уже существует!",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}
