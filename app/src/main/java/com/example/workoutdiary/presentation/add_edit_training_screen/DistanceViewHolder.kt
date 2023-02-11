package com.example.workoutdiary.presentation.add_edit_training_screen

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutdiary.data.model.relation_entities.ExerciseTrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import com.example.workoutdiary.databinding.TrainingDetailsItemBinding

class DistanceViewHolder(
    private val binding: TrainingDetailsItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(data: Pair<ExerciseTrainingBlock, List<ParameterizedSet>>) {
        binding.apply {
            trainingBlockNameTextView.text = data.first.exerciseName
            binding.setList.removeAllViews()
            data.second.forEach {
                val textView = TextView(binding.root.context)
                textView.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Body1)
                textView.text =
                    "Подход ${it.setOrder}: ${it.distance!!.toInt() / 1000} км ${it.distance!!.toInt() % 1000} м"
                binding.setList.addView(textView)
            }
        }
    }
}