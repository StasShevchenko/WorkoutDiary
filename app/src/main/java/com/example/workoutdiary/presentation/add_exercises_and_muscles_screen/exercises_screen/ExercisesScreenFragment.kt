package com.example.workoutdiary.presentation.add_exercises_and_muscles_screen.exercises_screen

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutdiary.R
import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.databinding.ExercisesScreenFragmentBinding
import com.example.workoutdiary.presentation.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExercisesScreenFragment : Fragment(R.layout.exercises_screen_fragment),
ExercisesAdapter.ExerciseClick{
    private val viewModel: ExercisesScreenViewModel by viewModels()
    private lateinit var rootView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ExercisesScreenFragmentBinding.bind(requireView())
        rootView = requireView().findViewById(R.id.root_layout)
        val exercisesAdapter = ExercisesAdapter(this)
        binding.apply {
            exerciseRecyclerView.apply {
                adapter = exercisesAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            exerciseNameEditText.addTextChangedListener { text ->
                viewModel.searchExercises(text.toString())
            }

            addExerciseButton.setOnClickListener{
                findNavController().navigate(R.id.addExerciseScreenFragment)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.exercises.collectLatest { exercises ->
                    exercisesAdapter.submitList(exercises)
                }
            }
        }




    }

    @SuppressLint("SuspiciousIndentation")
    override fun onDeleteClickListener() {
        val snackbar = Snackbar.make(
            rootView,
            getString(R.string.hold_to_delete),
            Snackbar.LENGTH_SHORT
        )
        snackbar.setAction("OK") {
            snackbar.dismiss()
        }
        snackbar.anchorView = (requireActivity() as MainActivity).findViewById(R.id.bottom_navigation_view)
            snackbar.show()
    }

    override fun onLongDeleteClickListener(exercise: Exercise) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.deletion))
            .setMessage(getString(R.string.exercise_delete_attention_message))
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.deleteExercise(exercise)
            }.create()
            .show()
    }

    override fun onExerciseClickListener(exercise: Exercise) {
        val bundle = Bundle()
        bundle.putInt("exerciseId", exercise.exerciseId)
        findNavController().navigate(R.id.addExerciseScreenFragment, bundle)
    }
}