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

class RepWeightItemViewHolder(
    private val onRepsEntered: ((Int, String) -> Unit)?,
    private val onWeightEntered: ((Int, String) -> Unit)?,
    private val context: Context,
    private val root: ViewGroup
) : SetsListViewItemViewHolder {
    private val repWeightItemView: View = LayoutInflater.from(context).inflate(
        R.layout.rep_weght_training_block_item,
        root,
        false
    )
    private val repsEditText =
        repWeightItemView.findViewById<TextInputEditText>(R.id.repweight_reps_edit_text)
    private val weightEditText =
        repWeightItemView.findViewById<TextInputEditText>(R.id.repweight_weight_edit_text)
    private val setNumberText = repWeightItemView.findViewById<TextView>(R.id.set_number_text_view)

    override fun bindData(itemPosition: Int, itemData: ParameterizedSet) {
        setNumberText.text =
            context.getString(R.string.set_number, itemPosition.toString())
        val repsNumber = if (itemData.repeats != null) itemData.repeats.toString() else ""
        repsEditText.setText(repsNumber)
        repsEditText.addTextChangedListener { text ->
            onRepsEntered?.invoke(itemPosition - 1, text.toString())
        }
        val weightNumber = if (itemData.weight != null) itemData.weight.toString() else ""
        weightEditText.setText(weightNumber)
        weightEditText.addTextChangedListener{ text ->
            onWeightEntered?.invoke(itemPosition - 1, text.toString())
        }
    }

    override fun validateItem(itemData: ValidateSet) {
        if (itemData.repsError) {
            repsEditText.error = context.getString(R.string.enter_data)
        }
        if (itemData.weightError) {
            weightEditText.error = context.getString(R.string.enter_data)
        }
    }

    override fun getView(): View {
        return repWeightItemView
    }
}