package com.example.workoutdiary.presentation.home_screen


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.ConfigurationCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutdiary.R
import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.databinding.TrainingItemBinding
import com.example.workoutdiary.domain.model.TrainingDay
import com.example.workoutdiary.presentation.utils.resolveColorAttr
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

class TrainingDaysAdapter(private val clickListener: OnTrainingClickListener) : ListAdapter<TrainingDay, TrainingDaysAdapter.TrainingDaysViewHolder>(
   TrainingDaysComparator()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingDaysViewHolder {
        val binding =
           TrainingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrainingDaysViewHolder(binding, ConfigurationCompat.getLocales(parent.context.resources.configuration)[0]!!)
    }

    override fun onBindViewHolder(holder: TrainingDaysViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class TrainingDaysViewHolder(private val binding: TrainingItemBinding, private val currentLocale: Locale) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = absoluteAdapterPosition
                if(position != RecyclerView.NO_POSITION){
                    val currentItem = getItem(position)
                    if(currentItem.training != null)
                    clickListener.onTrainingClick(getItem(position).training, currentItem.date)
                    else clickListener.onTrainingClick(null, currentItem.date)
                }
            }
        }

        @SuppressLint("SetTextI18n")
        fun bind(trainingDay: TrainingDay) {
            if(trainingDay.training != null){
                binding.apply {
                    dayTextView.setTextColor(resolveColorAttr(binding.root.context, com.google.android.material.R.attr.colorOnPrimarySurface))
                    trainingInfoTextView.setTextColor(resolveColorAttr(binding.root.context, com.google.android.material.R.attr.colorOnPrimarySurface))
                    trainingItem.setCardBackgroundColor(resolveColorAttr(binding.root.context, com.google.android.material.R.attr.colorPrimary))
                    dayTextView.text = (trainingDay.date.dayOfWeek.getDisplayName(TextStyle.FULL, currentLocale)  + "\n" + trainingDay.date.dayOfMonth)
                    if (trainingDay.training.trainingName.isNotBlank()) {
                        trainingInfoTextView.text = trainingDay.training.trainingName
                    } else {
                        trainingInfoTextView.text = binding.root.context.getString(R.string.without_name)
                    }
                }
            } else {
                binding.apply {
                    trainingItem.setCardBackgroundColor(resolveColorAttr(binding.root.context, com.google.android.material.R.attr.colorSurface))
                    dayTextView.setTextColor(resolveColorAttr(binding.root.context, com.google.android.material.R.attr.colorOnSurface))
                    trainingInfoTextView.setTextColor(resolveColorAttr(binding.root.context, com.google.android.material.R.attr.colorOnSurface))
                    dayTextView.text = (trainingDay.date.dayOfWeek.getDisplayName(TextStyle.FULL, currentLocale) + "\n" + trainingDay.date.dayOfMonth)
                    trainingInfoTextView.text = binding.root.context.getString(R.string.no_trainings)
                }
            }
        }
    }
    class TrainingDaysComparator : DiffUtil.ItemCallback<TrainingDay>() {
        override fun areItemsTheSame(oldItem: TrainingDay, newItem: TrainingDay): Boolean =
            oldItem.date == newItem.date


        override fun areContentsTheSame(oldItem: TrainingDay, newItem: TrainingDay): Boolean =
            oldItem == newItem

    }

    interface OnTrainingClickListener{
        fun onTrainingClick(training: Training?, date: LocalDate)
    }
}

