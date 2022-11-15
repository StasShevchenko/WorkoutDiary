package com.example.workoutdiary.presentation.add_exercises_and_muscles_screen.exercises_screen

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.accessibility.AccessibilityEventCompat.setAction
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
import com.example.workoutdiary.presentation.add_exercises_and_muscles_screen.exercises_screen.add_exercise_screen.AddExercisesScreenEvent
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
        rootView = getView()!!.findViewById(R.id.root_layout)
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

    override fun onDeleteClickListener() {
        val snackbar = Snackbar.make(
            rootView,
            "Удерживайте для удаления",
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
            .setTitle("Удаление")
            .setMessage("Внимание! Упражнение будет удалено из всех тренировок! Продолжить?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Да") { _, _ ->
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