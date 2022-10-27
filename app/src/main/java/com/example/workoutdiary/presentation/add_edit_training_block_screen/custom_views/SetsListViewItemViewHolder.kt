package com.example.workoutdiary.presentation.add_edit_training_block_screen.custom_views

import android.view.View
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import com.example.workoutdiary.presentation.add_edit_training_block_screen.ValidateSet

interface SetsListViewItemViewHolder {
    fun bindData(itemPosition: Int, itemData: ParameterizedSet)
    fun validateItem(itemData: ValidateSet)
    fun getView(): View
}