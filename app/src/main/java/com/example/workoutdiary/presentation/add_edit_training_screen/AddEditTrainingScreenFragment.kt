package com.example.workoutdiary.presentation.add_edit_training_screen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.workoutdiary.R
import com.example.workoutdiary.databinding.AddEditTrainingScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class AddEditTrainingScreenFragment : Fragment(R.layout.add_edit_training_screen) {
    private val viewModel: AddEditTrainingScreenViewModel by viewModels()

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
        }
    }
}