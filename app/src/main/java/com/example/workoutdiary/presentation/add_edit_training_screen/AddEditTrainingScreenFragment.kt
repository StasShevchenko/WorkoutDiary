package com.example.workoutdiary.presentation.add_edit_training_screen

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.workoutdiary.R
import com.example.workoutdiary.databinding.AddEditTrainingScreenBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AddEditTrainingScreenFragment : Fragment(R.layout.add_edit_training_screen),
    TrainingBlocksAdapter.OnTrainingBlockClickListener,
    TrainingBlocksAdapter.OnBlocksSwapListener,
    TrainingBlocksMoveCallback.ItemTouchHelperSwapFinishedCallback {
    private val viewModel: AddEditTrainingScreenViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = AddEditTrainingScreenBinding.bind(view)
        val trainingBlocksAdapter = TrainingBlocksAdapter(this, this)
        binding.apply {
            if (viewModel.date == LocalDate.now()) {
                dateTextView.text =
                    viewModel.date.format(DateTimeFormatter.ofPattern("dd.MM.yy")) + " " + getString(
                        R.string.today
                    )
            } else {
                dateTextView.text = viewModel.date.format(DateTimeFormatter.ofPattern("dd.MM.yy"))
            }
            favouriteImageButton.setOnClickListener {
                viewModel.onEvent(AddEditTrainingScreenEvent.FavouriteButtonPressed)
            }
            trainingBlocksRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = trainingBlocksAdapter
                val callback: ItemTouchHelper.Callback =
                    TrainingBlocksMoveCallback(
                        trainingBlocksAdapter,
                        this@AddEditTrainingScreenFragment
                    )
                val touchHelper = ItemTouchHelper(callback)
                touchHelper.attachToRecyclerView(this)
            }
            trainingNameEditText.setText(viewModel.trainingName)
            trainingNameEditText.addTextChangedListener {
                viewModel.onEvent(AddEditTrainingScreenEvent.NameEntered(it.toString()))
            }

            addTrainingBlockButton.setOnClickListener {
                val action = AddEditTrainingScreenFragmentDirections
                    .actionAddEditTrainingScreenFragmentToAddEditTrainingBlockScreenFragment(
                        trainingId = viewModel.currentTrainingId,
                        setOrder = viewModel.trainingDetails.value.size
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
                    is AddEditTrainingScreenViewModel.UiEvent.FavouriteChanged -> {
                        val message =
                            if (event.isFavourite) "Тренировка добавлена в избранные!" else "Тренировка удалена из избранных!"
                        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }

        onBackPressedCustomAction {
            viewModel.onEvent(AddEditTrainingScreenEvent.OnBackPressed)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.trainingDetails.collectLatest { trainingDetails ->
                        trainingBlocksAdapter.submitList(trainingDetails)
                    }
                }
                launch {
                    viewModel.currentTraining.collectLatest { currentTraining ->
                        currentTraining?.let {
                            if (currentTraining.isFavourite) {
                                binding.favouriteImageButton.setImageResource(R.drawable.ic_favourite_fill)
                            } else {
                                binding.favouriteImageButton.setImageResource(R.drawable.ic_favourite_border)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onTrainingBlockClick(
        trainingBlockId: Int,
        trainingBlockOrder: Int,
        exerciseId: Int
    ) {
        val action =
            AddEditTrainingScreenFragmentDirections.actionAddEditTrainingScreenFragmentToAddEditTrainingBlockScreenFragment(
                viewModel.currentTrainingId,
                trainingBlockId,
                trainingBlockOrder,
                exerciseId
            )
        findNavController().navigate(action)
    }

    override fun onBlockSwapListener(fromPosition: Int, toPosition: Int) {
        viewModel.onEvent(
            AddEditTrainingScreenEvent.TrainingBlockSwapped(
                fromPosition,
                toPosition
            )
        )
    }

    override fun rowSwapFinished() {
        viewModel.onEvent(
            AddEditTrainingScreenEvent.SwapFinished
        )
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
