package com.example.workoutdiary.presentation.add_exercises_and_muscles_screen.muscles_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutdiary.data.model.entities.Muscle
import com.example.workoutdiary.databinding.MuscleItemBinding

class MusclesAdapter(
    private val onClickListener: MuscleClick
) : ListAdapter<Muscle, MusclesAdapter.MuscleViewHolder>(MusclesComparator()) {

    inner class MuscleViewHolder(private val binding: MuscleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onClickListener.onMuscleClick(getItem(bindingAdapterPosition))
            }
            binding.deleteMuscleButton.setOnClickListener{
                onClickListener.onDeleteClick()
            }
            binding.deleteMuscleButton.setOnLongClickListener{
                onClickListener.onDeleteLongClick(getItem(bindingAdapterPosition))
                true
            }
        }

        fun bind(muscle: Muscle) {
            binding.apply {
                muscleNameTextView.text = muscle.muscleName
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MuscleViewHolder {
        val binding = MuscleItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MuscleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MuscleViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class MusclesComparator : DiffUtil.ItemCallback<Muscle>(){
        override fun areItemsTheSame(oldItem: Muscle, newItem: Muscle): Boolean {
            return oldItem.muscleId == newItem.muscleId
        }

        override fun areContentsTheSame(oldItem: Muscle, newItem: Muscle): Boolean {
            return  oldItem.muscleName == newItem.muscleName
        }
    }

    interface MuscleClick{
        fun onMuscleClick(muscle: Muscle)
        fun onDeleteClick()
        fun onDeleteLongClick(muscle: Muscle)
    }
}