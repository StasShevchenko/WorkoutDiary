package com.example.workoutdiary.presentation.custom_views

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.example.workoutdiary.R
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import com.example.workoutdiary.presentation.add_edit_training_block_screen.ValidateSet
import com.google.android.material.textfield.TextInputEditText


class SetsListView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private var dataIsInitiated = false
    private var currentExerciseType = ""

    private lateinit var setsLayout: LinearLayout
    private var onRepsEntered: ((Int, String) -> Unit)? = null
    private var onWeightEntered: ((Int, String) -> Unit)? = null


    fun setOnRepsEntered(listener: (Int, String) -> Unit) {
        onRepsEntered = listener
    }

    fun setOnWeightEntered(listener: (Int, String) -> Unit) {
        onWeightEntered = listener
    }

    init {
        initializeViews(context)
    }

    private fun initializeViews(context: Context) {
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as (LayoutInflater)
        inflater.inflate(R.layout.sets_list_view, this)
        setsLayout = findViewById(R.id.sets_linear_layout)
    }

    fun submitSetsList(validateSetsList: List<ValidateSet>, exerciseType: String) {
        val setsList = validateSetsList.map { it.setData }
        if (!dataIsInitiated && setsList.isNotEmpty()) {
            when (exerciseType) {
                "REPS" -> {
                    currentExerciseType = "REPS"
                    initiateRepItems(setsList.size - 1, setsList)
                    dataIsInitiated = true
                }
                "WEIGHT AND REPS" -> {
                    currentExerciseType = "WEIGHT AND REPS"
                    initiateWeightAndRepsItems(setsList.size - 1, setsList)
                    dataIsInitiated = true
                }
            }
        } else {
            when (exerciseType) {
                "REPS" -> {
                    //Adding views when exercise type has changed
                    if (currentExerciseType != "REPS") {
                        setsLayout.removeAllViews()
                        initiateRepItems(setsList.size - 1, setsList)
                        currentExerciseType = exerciseType
                        //Adding views when exercise type hasn't changed
                    } else {
                        //Add view when sets count was increased
                        if (setsList.size > setsLayout.childCount) {
                            val repItemView = LayoutInflater.from(context).inflate(
                                R.layout.rep_training_block_item,
                                this,
                                false
                            )
                            repItemView.findViewById<TextView>(R.id.set_number_text_view).text =
                                "Подход № " + setsList.size
                            repItemView.findViewById<TextInputEditText>(R.id.rep_reps_edit_text)
                                .addTextChangedListener { text ->
                                    onRepsEntered?.invoke(setsList.size - 1, text.toString())
                                }
                            setsLayout.addView(repItemView)
                            //Remove last view when sets count was decreased
                        } else if (setsList.size < setsLayout.childCount) {
                            setsLayout.removeViewAt(setsLayout.childCount - 1)
                        }
                    }
                }
                "WEIGHT AND REPS" -> {
                    //Adding views when exercise type has changed
                    if (currentExerciseType != "WEIGHT AND REPS") {
                        setsLayout.removeAllViews()
                        initiateWeightAndRepsItems(setsList.size - 1, setsList)
                        currentExerciseType = exerciseType
                    } else {
                        //Adding views when exercise type hasn't changed
                        if (setsList.size > setsLayout.childCount) {
                            val repWeightItemView = LayoutInflater.from(context).inflate(
                                R.layout.rep_weght_training_block_item,
                                this,
                                false
                            )
                            repWeightItemView.findViewById<TextView>(R.id.set_number_text_view).text =
                                "Подход № " + setsList.size
                            repWeightItemView.findViewById<TextInputEditText>(R.id.repweight_reps_edit_text)
                                .addTextChangedListener { text ->
                                    onRepsEntered?.invoke(setsList.size - 1, text.toString())
                                }
                            repWeightItemView.findViewById<TextInputEditText>(R.id.repweight_reps_edit_text)
                                .addTextChangedListener { text ->
                                    onWeightEntered?.invoke(setsList.size - 1, text.toString())
                                }
                            setsLayout.addView(repWeightItemView)
                            //Remove last view when sets count was decreased
                        } else if (setsList.size < setsLayout.childCount) {
                            setsLayout.removeViewAt(setsLayout.childCount - 1)
                        }
                    }
                }
            }
        }
        if(setsLayout.childCount != 0) validateItems(validateSetsList, currentExerciseType)
    }

    private fun initiateWeightAndRepsItems(count: Int, setsList: List<ParameterizedSet>) {
        for (i in 0..count) {
            val repWeightItemView = LayoutInflater.from(context).inflate(
                R.layout.rep_weght_training_block_item,
                this,
                false
            )
            repWeightItemView.findViewById<TextView>(R.id.set_number_text_view).text =
                "Подход № " + (i + 1)
            val repsEditText =
                repWeightItemView.findViewById<TextInputEditText>(R.id.repweight_reps_edit_text)
            val repText = if (setsList[i].repeats != null) setsList[i].repeats.toString() else ""
            repsEditText.setText(repText)
            repsEditText.addTextChangedListener { text ->
                onRepsEntered?.invoke(i, text.toString())
            }
            val weightEditText =
                repWeightItemView.findViewById<TextInputEditText>(R.id.repweight_weight_edit_text)
            val weightText = if (setsList[i].weight != null) setsList[i].weight.toString() else ""
            weightEditText.setText(weightText)
            weightEditText.addTextChangedListener { text ->
                onWeightEntered?.invoke(i, text.toString())
            }
            setsLayout.addView(repWeightItemView)
        }
    }


    private fun initiateRepItems(count: Int, setsList: List<ParameterizedSet>) {
        for (i in 0..count) {
            val repItemView = LayoutInflater.from(context).inflate(
                R.layout.rep_training_block_item,
                this,
                false
            )
            repItemView.findViewById<TextView>(R.id.set_number_text_view).text =
                "Подход № " + (i + 1)
            val repsEditText =
                repItemView.findViewById<TextInputEditText>(R.id.rep_reps_edit_text)
            val text = if (setsList[i].repeats != null) setsList[i].repeats.toString() else ""
            repsEditText.setText(text)
            repsEditText.addTextChangedListener { text ->
                onRepsEntered?.invoke(i, text.toString())
            }
            setsLayout.addView(repItemView)
        }
    }

    private fun validateItems(validateSet: List<ValidateSet>, exerciseType: String) {
        when (exerciseType) {
            "REPS" -> {
                for (i in validateSet.indices) {
                    val currentItem = setsLayout.getChildAt(i) as LinearLayout
                    val repItem =
                        currentItem.findViewById<TextInputEditText>(R.id.rep_reps_edit_text)
                    if (validateSet[i].repsError) {
                        repItem.error = "Введите данные!"
                    }
                }
            }
            "WEIGHT AND REPS" -> {
                for (i in validateSet.indices) {
                    val currentItem = setsLayout.getChildAt(i) as LinearLayout
                    val repItem =
                        currentItem.findViewById<TextInputEditText>(R.id.repweight_reps_edit_text)
                    if (validateSet[i].repsError) {
                        repItem.error = "Введите данные!"
                    }
                    val weightItem =
                        currentItem.findViewById<TextInputEditText>(R.id.repweight_weight_edit_text)
                    if (validateSet[i].weightError) {
                        weightItem.error = "Введите данные!"
                    }
                }
            }
        }
    }
}