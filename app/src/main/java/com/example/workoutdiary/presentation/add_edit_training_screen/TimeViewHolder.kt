package com.example.workoutdiary.presentation.add_edit_training_screen

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutdiary.R
import com.example.workoutdiary.data.model.relation_entities.ExerciseTrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import com.example.workoutdiary.databinding.TrainingDetailsItemBinding

class TimeViewHolder(
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
                    "Подход ${it.setOrder}: ${it.time!!.toInt() / 60} мин. ${it.time!!.toInt() % 60} сек."
                binding.setList.addView(textView)
            }
        }
    }
}