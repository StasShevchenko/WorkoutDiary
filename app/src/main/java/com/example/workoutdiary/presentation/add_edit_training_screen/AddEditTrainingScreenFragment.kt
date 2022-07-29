package com.example.workoutdiary.presentation.add_edit_training_screen

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.workoutdiary.R
import com.example.workoutdiary.databinding.AddEditTrainingScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
        binding.root.visibility = View.INVISIBLE

        binding.apply {
            if (viewModel.date == LocalDate.now()) {
                dateTextView.text =
                    viewModel.date.format(DateTimeFormatter.ofPattern("dd.MM.yy")) + " " + getString(
                        R.string.today
                    )
            } else {
                dateTextView.text = viewModel.date.format(DateTimeFormatter.ofPattern("dd.MM.yy"))
            }

            trainingNameEditText.setText(viewModel.trainingName)
            trainingNameEditText.addTextChangedListener {
                viewModel.onEvent(AddEditTrainingScreenEvent.NameEntered(it.toString()))
            }

            addTrainingBlockButton.setOnClickListener {
                val action = AddEditTrainingScreenFragmentDirections
                    .actionAddEditTrainingScreenFragmentToAddEditTrainingBlockScreenFragment(
                        trainingId = viewModel.currentTrainingId,
                        setOrder = viewModel.trainingDetails.value.keys.size
                    )
                findNavController().navigate(action)
            }
            deleteTrainingButton.setOnClickListener {
                AlertDialog.Builder(requireContext())
                    .setTitle("Удалить тренировку")
                    .setMessage("Вы действительно хотите удалить тренировку?")
                    .setIcon(R.drawable.ic_delete)
                    .setPositiveButton("Да") { _, _ ->
                        viewModel.onEvent(AddEditTrainingScreenEvent.DeletePressed)
                    }.create()
                    .show()

            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is AddEditTrainingScreenViewModel.UiEvent.OnBackPressed -> {
                        findNavController().popBackStack()
                    }
                    AddEditTrainingScreenViewModel.UiEvent.OnDeletePressed -> {
                        findNavController().popBackStack()
                    }
                }
            }
        }

        onBackPressedCustomAction {
            viewModel.onEvent(AddEditTrainingScreenEvent.OnBackPressed)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.trainingDetails.collectLatest { trainingDetails ->
                    if ((viewModel.isCurrentItemReceived) || viewModel.isNewEntryReceived) {
                        binding.trainingBlocksList.removeAllViews()
                        trainingDetails.forEach { trainingBlock ->
                            val trainingDetailsItemBinding = LayoutInflater.from(requireContext())
                                .inflate(
                                    R.layout.training_details_item,
                                    binding.trainingBlocksList,
                                    false
                                )
                            trainingDetailsItemBinding.findViewById<TextView>(R.id.training_block_name_text_view).text =
                                trainingBlock.key.exerciseName
                            trainingDetailsItemBinding.setOnClickListener {
                                val action =
                                    AddEditTrainingScreenFragmentDirections.actionAddEditTrainingScreenFragmentToAddEditTrainingBlockScreenFragment(
                                        viewModel.currentTrainingId,
                                        trainingBlock.key.trainingBlockId,
                                        trainingBlock.key.trainingBlockOrder,
                                        trainingBlock.key.exerciseId
                                    )
                                findNavController().navigate(action)
                            }
                            when (trainingBlock.key.exerciseType) {
                                "WEIGHT AND REPS" -> {
                                    trainingBlock.value.forEach {
                                        val textView = TextView(requireContext())
                                        textView.setTextAppearance(androidx.constraintlayout.widget.R.style.TextAppearance_AppCompat_Body1)
                                        textView.text =
                                            "Подход ${it.setOrder}: ${it.repeats} повторений ${it.weight} кг"
                                        trainingDetailsItemBinding.findViewById<LinearLayout>(R.id.set_list)
                                            .addView(textView)
                                    }
                                }
                                "REPS" -> {
                                    trainingBlock.value.forEach {
                                        val textView = TextView(requireContext())
                                        textView.setTextAppearance(androidx.constraintlayout.widget.R.style.TextAppearance_AppCompat_Body1)
                                        textView.text =
                                            "Подход ${it.setOrder}: ${it.repeats} повторений"
                                        trainingDetailsItemBinding.findViewById<LinearLayout>(R.id.set_list)
                                            .addView(textView)
                                    }
                                }
                            }
                            binding.trainingBlocksList.addView(trainingDetailsItemBinding)
                        }
                        binding.root.visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}

fun Fragment.onBackPressedCustomAction(action: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(
        viewLifecycleOwner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                action()
            }
        })
}
