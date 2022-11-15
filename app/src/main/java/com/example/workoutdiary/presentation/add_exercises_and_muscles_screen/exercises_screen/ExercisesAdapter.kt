package com.example.workoutdiary.presentation.add_exercises_and_muscles_screen.exercises_screen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.databinding.ExerciseItemBinding

class ExercisesAdapter(
    private val exerciseClick: ExerciseClick
) :
    ListAdapter<Exercise, ExercisesAdapter.ExerciseViewHolder>(ExerciseComparator()) {


    inner class ExerciseViewHolder(private val binding: ExerciseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init{
            binding.root.setOnClickListener{
                exerciseClick.onExerciseClickListener(getItem(bindingAdapterPosition))
            }
            binding.deleteExerciseImageButton.setOnClickListener {
                exerciseClick.onDeleteClickListener()
            }
            binding.deleteExerciseImageButton.setOnLongClickListener{
                exerciseClick.onLongDeleteClickListener(getItem(bindingAdapterPosition))
                true
            }
        }
        fun bind(exercise: Exercise) {
            binding.apply {
                exerciseNameTextView.text = exercise.exerciseName
                if (exercise.exerciseDescription == null) {
                    descriptionTextView.visibility = View.GONE
                    divider.visibility = View.GONE
                }
                else {
                    descriptionTextView.text = exercise.exerciseDescription
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val binding =
            ExerciseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExerciseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = getItem(position)
        holder.bind(exercise)
    }

    class ExerciseComparator : DiffUtil.ItemCallback<Exercise>() {
        override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return oldItem.exerciseId == newItem.exerciseId
        }

        override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
            return true
        }
    }

    interface ExerciseClick{
        fun onExerciseClickListener(exercise: Exercise)
        fun onDeleteClickListener()
        fun onLongDeleteClickListener(exercise: Exercise)
    }
}