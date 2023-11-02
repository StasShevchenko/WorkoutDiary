package com.example.workoutdiary.presentation.exercise_details_screen

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.workoutdiary.R
import com.example.workoutdiary.databinding.ExerciseDetailsFragmentBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExerciseDetailsFragment : Fragment(R.layout.exercise_details_fragment) {
    private val viewModel: ExerciseDetailsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = ExerciseDetailsFragmentBinding.bind(view)


        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                viewModel.currentExercise.collectLatest { it ->
                    it?.let { exercise ->
                        binding.apply {
                            exerciseHeaderTextView.text = exercise.exerciseName
                            exerciseDescription.text = exercise.exerciseDescription
                            exercise.imagePath?.let {
                                exerciseImage.visibility = View.VISIBLE
                                Picasso.get()
                                    .load(Uri.parse(exercise.imagePath))
                                    .error(R.drawable.ic_broken_image)
                                    .into(exerciseImage, object : Callback {
                                        override fun onSuccess() {
                                            exerciseImage.scaleType = ImageView.ScaleType.FIT_CENTER
                                        }

                                        override fun onError(e: Exception?) {
                                        }
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}