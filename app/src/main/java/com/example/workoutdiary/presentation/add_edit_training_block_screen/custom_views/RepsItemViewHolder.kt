package com.example.workoutdiary.presentation.add_edit_training_block_screen.custom_views

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.example.workoutdiary.R
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import com.example.workoutdiary.presentation.add_edit_training_block_screen.ValidateSet
import com.google.android.material.textfield.TextInputEditText

class RepsItemViewHolder(
    private val onRepsEntered: ((Int, String) -> Unit)?,
    private val context: Context,
    private val root: ViewGroup
) : SetsListViewItemViewHolder {
    private val repItemView: View = LayoutInflater.from(context).inflate(
        R.layout.rep_training_block_item,
        root,
        false
    )
    private val repsEditText = repItemView.findViewById<TextInputEditText>(R.id.rep_reps_edit_text)
    private val setNumberText = repItemView.findViewById<TextView>(R.id.set_number_text_view)

    override fun bindData(itemPosition: Int, itemData: ParameterizedSet) {
        setNumberText.text =
            "Подход № $itemPosition"
        val repsNumber = if (itemData.repeats != null) itemData.repeats.toString() else ""
        repsEditText.setText(repsNumber)
        repsEditText.addTextChangedListener { text ->
            onRepsEntered?.invoke(itemPosition - 1, text.toString())
        }
    }

    override fun validateItem(itemData: ValidateSet) {
        if (itemData.repsError) {
            repsEditText.error = "Введите данные!"
        }
    }

    override fun getView(): View{
        return repItemView
    }
}