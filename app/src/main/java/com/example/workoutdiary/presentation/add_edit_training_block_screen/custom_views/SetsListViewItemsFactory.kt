package com.example.workoutdiary.presentation.add_edit_training_block_screen.custom_views

import android.content.Context
import android.view.ViewGroup
import com.example.workoutdiary.utils.ExerciseType

class SetsListViewItemsFactory(
    private val context: Context,
    private val root: ViewGroup
) {
    private var onRepsEntered: ((Int, String) -> Unit)? = null
    private var onWeightEntered: ((Int, String) -> Unit)? = null

    fun getItem(itemType: ExerciseType) : SetsListViewItemViewHolder {
        when (itemType) {
            ExerciseType.REPS -> {
                return RepsItemViewHolder(
                    onRepsEntered,
                    context,
                    root
                )
            }
            ExerciseType.WEIGHT_AND_REPS -> {
                return RepWeightItemViewHolder(
                    onRepsEntered,
                    onWeightEntered,
                    context,
                    root
                )
            }
            else -> {
                return RepsItemViewHolder(
                    onRepsEntered,
                    context,
                    root
                )
            }
        }
    }

    fun setOnRepsEntered(listener: (Int, String) -> Unit) {
        onRepsEntered = listener

    }

    fun setOnWeightEntered(listener: (Int, String) -> Unit) {
        onWeightEntered = listener
    }

}