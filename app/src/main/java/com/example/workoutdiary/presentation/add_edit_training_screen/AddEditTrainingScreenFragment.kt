package com.example.workoutdiary.presentation.add_edit_training_screen

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.workoutdiary.R
import com.example.workoutdiary.databinding.AddEditTrainingScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AddEditTrainingScreenFragment : Fragment(R.layout.add_edit_training_screen) {
    private val viewModel: AddEditTrainingScreenViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = AddEditTrainingScreenBinding.bind(view)
        binding.apply {
            if(viewModel.date == LocalDate.now()) {
                dateTextView.text = viewModel.date.format(DateTimeFormatter.ofPattern("dd.MM.yy")) +" "+ getString(R.string.today)
            }
            else{
                dateTextView.text = viewModel.date.format(DateTimeFormatter.ofPattern("dd.MM.yy"))
            }

            trainingNameEditText.setText(viewModel.trainingName)
            trainingNameEditText.addTextChangedListener {
                viewModel.onEvent(AddEditTrainingScreenEvent.NameEntered(it.toString()))
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is AddEditTrainingScreenViewModel.UiEvent.OnBackPressed -> {
                        findNavController().popBackStack()
                    }
                }
            }
        }

        onBackPressedCustomAction {
            viewModel.onEvent(AddEditTrainingScreenEvent.OnBackPressed)
        }

        viewModel.trainingDetails.observe(viewLifecycleOwner){ trainingDetails ->
            trainingDetails.forEach{ trainingBlock ->
                val trainingDetailsItemBinding = LayoutInflater.from(requireContext()).inflate(R.layout.training_details_item, binding.trainingBlocksList, false)
                trainingDetailsItemBinding.findViewById<TextView>(R.id.training_block_name_text_view).text = trainingBlock.key.exerciseName
                when (trainingBlock.key.exerciseType) {
                    "Вес и повторения" -> {
                        trainingBlock.value.forEach{
                            val textView = TextView(requireContext())
                            textView.setTextAppearance(androidx.constraintlayout.widget.R.style.TextAppearance_AppCompat_Body1)
                            textView.text = "Подход ${it.setOrder}: ${it.repeats} повторений ${it.weight} кг"
                            trainingDetailsItemBinding.findViewById<LinearLayout>(R.id.set_list).addView(textView)
                        }
                    }
                    "Повторения" ->{
                        trainingBlock.value.forEach{
                            val textView = TextView(requireContext())
                            textView.setTextAppearance(androidx.constraintlayout.widget.R.style.TextAppearance_AppCompat_Body1)
                            textView.text = "Подход ${it.setOrder}: ${it.repeats} повторений"
                            trainingDetailsItemBinding.findViewById<LinearLayout>(R.id.set_list).addView(textView)
                        }
                    }
                }
                binding.trainingBlocksList.addView(trainingDetailsItemBinding)
            }
        }
    }
}

fun Fragment.onBackPressedCustomAction(action: () -> Unit){
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            action()
        }
    })
}
