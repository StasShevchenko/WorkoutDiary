package com.example.workoutdiary.presentation.home_screen


import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.ConfigurationCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutdiary.R
import com.example.workoutdiary.databinding.TrainingItemBinding
import com.example.workoutdiary.domain.model.TrainingDay
import java.time.format.TextStyle
import java.util.*

class TrainingDaysAdapter : ListAdapter<TrainingDay, TrainingDaysAdapter.TrainingDaysViewHolder>(
    TrainingDaysAdapter.TrainingDaysComparator()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingDaysViewHolder {
        val binding =
           TrainingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrainingDaysViewHolder(binding, ConfigurationCompat.getLocales(parent.context.resources.configuration)[0])
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TrainingDaysViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    class TrainingDaysViewHolder(private val binding: TrainingItemBinding, private val currentLocale: Locale) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(trainingDay: TrainingDay) {
            if (trainingDay.trainingList.isEmpty()) {
                binding.apply {
                    dayTextView.text = (trainingDay.date.dayOfWeek.getDisplayName(TextStyle.FULL, currentLocale) + "\n" + trainingDay.date.dayOfMonth)
                    trainingInfoTextView.text = binding.root.context.getString(R.string.no_trainings)
                }
            }
            if(trainingDay.trainingList.size == 1){
                binding.apply {
                    dayTextView.text = (trainingDay.date.dayOfWeek.getDisplayName(TextStyle.FULL, currentLocale)  + "\n" + trainingDay.date.dayOfMonth)
                    trainingInfoTextView.text = trainingDay.trainingList[0].trainingName
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
}