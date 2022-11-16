package com.example.workoutdiary.presentation.add_exercises_and_muscles_screen.muscles_screen

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
import com.example.workoutdiary.data.model.entities.Muscle
import com.example.workoutdiary.databinding.MusclesFragmentBinding
import com.example.workoutdiary.presentation.MainActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MusclesFragment : Fragment(R.layout.muscles_fragment), MusclesAdapter.MuscleClick {

    private val viewModel: MusclesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = MusclesFragmentBinding.bind(view)
        val musclesAdapter = MusclesAdapter(this)

        binding.apply {
            musclesRecycleView.apply {
                adapter = musclesAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }
            searchMuscleEditText.addTextChangedListener { text ->
                viewModel.searchMuscles(text.toString().trim())
            }
            addMuscleButton.setOnClickListener {
                findNavController().navigate(R.id.addMuscleScreenFragment)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.muscles.collectLatest { muscles ->
                    musclesAdapter.submitList(muscles)
                }
            }
        }

    }


    override fun onDeleteClick() {
        val snackbar = Snackbar.make(
            requireView(),
            "Удерживайте для удаления",
            Snackbar.LENGTH_SHORT
        )
        snackbar.anchorView =
            (requireActivity() as MainActivity).findViewById(R.id.bottom_navigation_view)
        snackbar.setAction("OK") {
            snackbar.dismiss()
        }
        snackbar.show()
    }

    override fun onDeleteLongClick(muscle: Muscle) {
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление")
            .setMessage("Внимание! Все упражнения данной группы будут удалены из всех тренировок! Продолжить?")
            .setIcon(R.drawable.ic_delete)
            .setPositiveButton("Да") { _, _ ->
                viewModel.deleteMuscle(muscle)
            }.create()
            .show()
    }

    override fun onMuscleClick(muscle: Muscle) {
        val bundle = Bundle()
        bundle.putInt("muscleId", muscle.muscleId)
        findNavController().navigate(R.id.addMuscleScreenFragment, bundle)
    }
}