package com.example.workoutdiary.presentation.home_screen


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.ConfigurationCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutdiary.R
import com.example.workoutdiary.databinding.TrainingItemBinding
import com.example.workoutdiary.domain.model.TrainingDay
import com.example.workoutdiary.presentation.utils.resolveColorAttr
import com.google.android.material.color.MaterialColors
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

    override fun onBindViewHolder(holder: TrainingDaysViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    class TrainingDaysViewHolder(private val binding: TrainingItemBinding, private val currentLocale: Locale) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(trainingDay: TrainingDay) {
            if (trainingDay.trainingList.isEmpty()) {
                binding.apply {
                    trainingItem.setCardBackgroundColor(resolveColorAttr(binding.root.context, com.google.android.material.R.attr.colorSurface))
                    dayTextView.setTextColor(resolveColorAttr(binding.root.context, com.google.android.material.R.attr.colorOnSurface))
                    trainingInfoTextView.setTextColor(resolveColorAttr(binding.root.context, com.google.android.material.R.attr.colorOnSurface))
                    dayTextView.text = (trainingDay.date.dayOfWeek.getDisplayName(TextStyle.FULL, currentLocale) + "\n" + trainingDay.date.dayOfMonth)
                    trainingInfoTextView.text = binding.root.context.getString(R.string.no_trainings)
                }
            }
            if(trainingDay.trainingList.size == 1){
                binding.apply {
                    dayTextView.setTextColor(resolveColorAttr(binding.root.context, androidx.appcompat.R.attr.colorPrimary))
                    trainingInfoTextView.setTextColor(resolveColorAttr(binding.root.context, com.google.android.material.R.attr.colorOnPrimarySurface))
                    trainingItem.setCardBackgroundColor(resolveColorAttr(binding.root.context, com.google.android.material.R.attr.colorOnPrimarySurface))
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

