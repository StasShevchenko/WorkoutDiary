package com.example.workoutdiary.presentation.add_edit_training_block_screen.custom_views

import android.content.Context
import android.view.ViewGroup

class SetsListViewItemsFactory(
    private val context: Context,
    private val root: ViewGroup
) {
    private var onRepsEntered: ((Int, String) -> Unit)? = null
    private var onWeightEntered: ((Int, String) -> Unit)? = null

    fun getItem(itemType: String) : SetsListViewItemViewHolder {
        when (itemType) {
            "REPS" -> {
                return RepsItemViewHolder(
                    onRepsEntered,
                    context,
                    root
                )
            }
            "WEIGHT AND REPS" -> {
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