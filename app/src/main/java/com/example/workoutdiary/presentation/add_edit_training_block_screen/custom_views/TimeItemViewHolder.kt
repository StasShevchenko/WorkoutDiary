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

class TimeItemViewHolder(
    private val onTimeEntered: ((Int, String) -> Unit)?,
    private val context: Context,
    val root: ViewGroup
) : SetsListViewItemViewHolder {

    private val timeItemView: View = LayoutInflater.from(context).inflate(
        R.layout.time_training_block_item,
        root,
        false
    )

    private val minutesEditText =
        timeItemView.findViewById<TextInputEditText>(R.id.time_minutes_edit_text)
    private val secondsEditText =
        timeItemView.findViewById<TextInputEditText>(R.id.time_seconds_edit_text)
    private val setNumberText = timeItemView.findViewById<TextView>(R.id.set_number_text_view)


    override fun bindData(itemPosition: Int, itemData: ParameterizedSet) {
        setNumberText.text =
            "Подход № $itemPosition"
        val minutesNumber = if (itemData.time != null) (itemData.time / 60).toString() else ""
        minutesEditText.setText(minutesNumber)
        minutesEditText.addTextChangedListener { text ->
            val secondsAmount =
                if (secondsEditText.text.toString().isNotEmpty()) secondsEditText.text.toString()
                    .toInt() else 0
            val minutesAmount = if (text.toString().isNotEmpty()) text.toString().toInt() else 0
            val resultTime = secondsAmount + minutesAmount * 60
            onTimeEntered?.invoke(itemPosition - 1, resultTime.toString())
        }
        val secondsNumber = if (itemData.time != null) (itemData.time % 60).toString() else ""
        secondsEditText.setText(secondsNumber)
        secondsEditText.addTextChangedListener { text ->
            val secondsAmount = if (text.toString().isNotEmpty()) text.toString().toInt() else 0
            val minutesAmount =
                if (minutesEditText.text.toString().isNotEmpty()) minutesEditText.text.toString()
                    .toInt() else 0
            val resultTime = secondsAmount + minutesAmount * 60
            onTimeEntered?.invoke(itemPosition - 1, resultTime.toString())
        }
    }

    override fun validateItem(itemData: ValidateSet) {
        if (itemData.timeError) {
            secondsEditText.error = "Введите данные"
        }
    }

    override fun getView(): View {
        return timeItemView
    }
}