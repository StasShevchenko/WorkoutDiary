package com.example.workoutdiary.presentation.new_trainings_variants_screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.workoutdiary.R
import com.example.workoutdiary.databinding.NewTrainingVariantsFragmentBinding


class NewTrainingVariantsFragment : Fragment(R.layout.new_training_variants_fragment) {
    private lateinit var binding: NewTrainingVariantsFragmentBinding
    private val args: NewTrainingVariantsFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = NewTrainingVariantsFragmentBinding.bind(view)
        val date = args.trainingDate

        binding.createNewTextView.setOnClickListener {
            val action = NewTrainingVariantsFragmentDirections.actionNewTrainingVariantsFragmentToAddEditTrainingScreenFragment(
                trainingDate = date,
                trainingId = -1,
                trainingName = "",
                isFromFavourites = false
            )
            findNavController().navigate(action)
        }

        binding.createFromFavouritesTextView.setOnClickListener {
            val action = NewTrainingVariantsFragmentDirections.actionNewTrainingVariantsFragmentToFavouriteTrainingsFragment(
                trainingDate = date
            )
            findNavController().navigate(action)
        }
    }
}