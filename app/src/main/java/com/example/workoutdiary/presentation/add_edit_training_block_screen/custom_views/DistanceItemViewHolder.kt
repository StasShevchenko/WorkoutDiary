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

class DistanceItemViewHolder(
    private val onDistanceEntered: ((Int, String) -> Unit)?,
    private val context: Context,
    val root: ViewGroup
) : SetsListViewItemViewHolder {

    private val distanceItemView: View = LayoutInflater.from(context).inflate(
        R.layout.distance_training_block_item,
        root,
        false
    )

    private val kilometersEditText =
        distanceItemView.findViewById<TextInputEditText>(R.id.distance_kilometers_edit_text)
    private val metersEditText =
        distanceItemView.findViewById<TextInputEditText>(R.id.distance_meters_edit_text)
    private val setNumberText = distanceItemView.findViewById<TextView>(R.id.set_number_text_view)

    override fun bindData(itemPosition: Int, itemData: ParameterizedSet) {
        setNumberText.text =
            "Подход № $itemPosition"
        val kilometersNumber =
            if (itemData.distance != null) (itemData.distance / 1000).toString() else ""
        kilometersEditText.setText(kilometersNumber)
        kilometersEditText.addTextChangedListener { text ->
            val metersAmount =
                if (metersEditText.text.toString().isNotEmpty()) metersEditText.text.toString()
                    .toInt() else 0
            val kilometersAmount =
                if (text.toString()
                        .isNotEmpty()
                ) text.toString().toInt() else 0
            val resultDistance = metersAmount + kilometersAmount * 1000
            onDistanceEntered?.invoke(itemPosition - 1, resultDistance.toString())
        }
        val metersAmount =
            if (itemData.distance != null) (itemData.distance % 1000).toString() else ""
        metersEditText.setText(metersAmount)
        metersEditText.addTextChangedListener { text ->
            val metersAmount =
                if (text.toString().isNotEmpty()) text.toString().toInt() else 0
            val kilometersAmount =
                if (kilometersEditText.text.toString()
                        .isNotEmpty()
                ) kilometersEditText.text.toString().toInt() else 0
            val resultDistance = metersAmount + kilometersAmount * 1000
            onDistanceEntered?.invoke(itemPosition - 1, resultDistance.toString())
        }
    }

    override fun validateItem(itemData: ValidateSet) {
        if (itemData.distanceError) {
            metersEditText.error = "Введите данные"
        }
    }

    override fun getView(): View {
        return distanceItemView
    }
}