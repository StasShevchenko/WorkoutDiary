package com.example.workoutdiary.presentation.add_exercises_and_muscles_screen.exercises_screen.add_exercise_screen

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.workoutdiary.R
import com.example.workoutdiary.databinding.AddExerciseScreenFragmentBinding
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddExerciseScreenFragment : Fragment(R.layout.add_exercise_screen_fragment) {
    private val viewModel: AddExerciseScreenViewModel by viewModels()
    private lateinit var binding: AddExerciseScreenFragmentBinding

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            val flag = Intent.FLAG_GRANT_READ_URI_PERMISSION
            requireContext().contentResolver.takePersistableUriPermission(uri, flag)
            binding.imagePicker.scaleType = ImageView.ScaleType.CENTER_CROP
            val encodedUri = uri.toString()
            viewModel.onEvent(AddExercisesScreenEvent.ImagePathUpdated(encodedUri))
            val decoded = Uri.parse(encodedUri)
            Picasso.get()
                .load(decoded)
                .error(R.drawable.ic_broken_image)
                .into(binding.imagePicker)
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddExerciseScreenFragmentBinding.bind(requireView())
        val exerciseMapper = ExerciseTypeMapper(resources.getStringArray(R.array.exercise_types))
        if (arguments?.getInt("exerciseId") != null) {
            binding.addExerciseButton.text = getString(R.string.update_exercise)
            binding.headerTextView.text = getString(R.string.exercise_updating)
        }
        binding.apply {
            exerciseTypeChoiceView.setText(resources.getStringArray(R.array.exercise_types)[0])
            val exerciseTypeAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_list_item_1,
                resources.getStringArray(R.array.exercise_types)
            )
            exerciseTypeChoiceView.setAdapter(exerciseTypeAdapter)
            exerciseNameEditText.addTextChangedListener { text ->
                viewModel.onEvent(AddExercisesScreenEvent.ExerciseNameEntered(text.toString()))
            }
            exerciseTypeChoiceView.setOnItemClickListener { _, _, i, _ ->
                viewModel.onEvent(
                    AddExercisesScreenEvent.ExerciseTypeSelected(
                        exerciseMapper.getValueByPosition(
                            i
                        )
                    )
                )
            }
            exerciseDescriptionEditText.addTextChangedListener { text ->
                viewModel.onEvent(AddExercisesScreenEvent.ExerciseDescriptionEntered(text.toString()))
            }
            addExerciseButton.setOnClickListener {
                viewModel.onEvent(AddExercisesScreenEvent.ExerciseAdded)
            }
            imagePicker.setOnClickListener{

                // Launch the photo picker and let the user choose only images.
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentExercise.collectLatest { exercise ->
                    exercise?.let { exercise ->
                        binding.apply {
                            exerciseNameEditText.setText(exercise.exerciseName)
                            exerciseDescriptionEditText.setText(exercise.exerciseDescription)
                            exerciseTypeChoiceView.setText(exerciseMapper.getNameByValue(exercise.exerciseType))
                            exercise.imagePath?.let{
                                Picasso.get()
                                    .load(exercise.imagePath)
                                    .error(R.drawable.ic_broken_image)
                                    .into(binding.imagePicker, object : Callback{
                                        override fun onSuccess() {
                                            binding.imagePicker.scaleType = ImageView.ScaleType.CENTER_CROP
                                        }
                                        override fun onError(e: Exception?) {
                                        }
                                    })
                            }
                            val exerciseTypeAdapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_list_item_1,
                                resources.getStringArray(R.array.exercise_types)
                            )
                            exerciseTypeChoiceView.setAdapter(exerciseTypeAdapter)
                            muscleGroupChoiceView.setText(viewModel.currentMuscle!!.muscleName)
                        }
                    }
                }
            }
        }


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.muscles.collectLatest { muscles ->
                    val musclesAdapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        muscles
                    )
                    binding.muscleGroupChoiceView.setAdapter(musclesAdapter)
                    binding.muscleGroupChoiceView.setOnItemClickListener { _, _, i, _ ->
                        viewModel.onEvent(AddExercisesScreenEvent.MuscleSelected(muscles[i]))
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collectLatest { uiEvent ->
                when (uiEvent) {
                    AddExerciseScreenViewModel.UiEvent.AddPressed -> {
                        findNavController().popBackStack()
                    }
                    AddExerciseScreenViewModel.UiEvent.UpdatePressed -> {
                        AlertDialog.Builder(requireContext())
                            .setTitle(getString(R.string.exercise_updating))
                            .setMessage(getString(R.string.training_update_attention_message))
                            .setPositiveButton("Да") { _, _ ->
                                viewModel.onEvent(AddExercisesScreenEvent.UpdateConfirmed)
                            }.create()
                            .show()
                    }
                    AddExerciseScreenViewModel.UiEvent.ExerciseAlreadyExists -> {
                        Snackbar.make(
                            view,
                            getString(R.string.exercise_exists),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    AddExerciseScreenViewModel.UiEvent.ValidationFailed -> {
                        Snackbar.make(
                            view,
                            getString(R.string.enter_all_fields),
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }


    }

}