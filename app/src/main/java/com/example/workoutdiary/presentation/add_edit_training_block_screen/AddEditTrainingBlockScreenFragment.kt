package com.example.workoutdiary.presentation.add_edit_training_block_screen

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
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
                    Log.d("MY DEBUG", "Muscle collected")
                    val stringList: MutableList<String> = arrayListOf()
                    muscles.forEach {
                        stringList.add(it.muscleName)
                    }
                    val muscleAdapter =
                        ArrayAdapter(
                            requireContext(),
                            R.layout.muscle_drop_down_item,
                            muscles
                        )
                    binding.muscleChoiceView.setAdapter(muscleAdapter)
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
    }
}
