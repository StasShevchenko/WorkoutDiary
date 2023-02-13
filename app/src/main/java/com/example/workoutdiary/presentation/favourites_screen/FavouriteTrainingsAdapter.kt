package com.example.workoutdiary.presentation.favourites_screen

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutdiary.R
import com.example.workoutdiary.data.model.entities.Training
import com.example.workoutdiary.databinding.FavouriteItemBinding
import java.time.format.DateTimeFormatter

class FavouriteTrainingsAdapter(
    private val clickListener: OnFavouriteTrainingClickListener
) : ListAdapter<Training, FavouriteTrainingsAdapter.FavouriteTrainingViewHolder>(
    FavouriteTrainingsComparator()
)
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteTrainingViewHolder {
        val binding =
            FavouriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavouriteTrainingViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: FavouriteTrainingViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class FavouriteTrainingViewHolder(
        private val binding: FavouriteItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = absoluteAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val currentItem = getItem(position)
                    if (currentItem != null) {
                        clickListener.onFavouriteTrainingClick(currentItem)
                    }
                }
            }
        }
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(training: Training) {
            binding.apply {
                favouriteTrainingNameTextView.text = training.trainingName
                val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", binding.root.context.resources.configuration.locales.get(0))
                favouriteTrainingDateTextView.text = binding.root.resources.getString(R.string.last_training, training.trainingDate.format(dateFormatter))
            }
        }
    }

    class FavouriteTrainingsComparator : DiffUtil.ItemCallback<Training>(){
        override fun areContentsTheSame(oldItem: Training, newItem: Training): Boolean =
            oldItem == newItem

        override fun areItemsTheSame(oldItem: Training, newItem: Training): Boolean =
            oldItem.trainingId == newItem.trainingId
    }

    interface OnFavouriteTrainingClickListener{
        fun onFavouriteTrainingClick(training: Training)
    }

}