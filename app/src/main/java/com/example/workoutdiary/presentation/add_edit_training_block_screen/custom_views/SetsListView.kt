package com.example.workoutdiary.presentation.add_edit_training_block_screen.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import com.example.workoutdiary.R
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import com.example.workoutdiary.presentation.add_edit_training_block_screen.ValidateSet
import com.example.workoutdiary.utils.ExerciseType
import com.google.android.material.textfield.TextInputEditText


class SetsListView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private var currentExerciseType: ExerciseType? = null
    private lateinit var setsLayout: LinearLayout
    private var onRepsEntered: ((Int, String) -> Unit)? = null
    private var onWeightEntered: ((Int, String) -> Unit)? = null
    private val itemsFactory: SetsListViewItemsFactory = SetsListViewItemsFactory(
        context,
        this
    )

    private val holdersList: MutableList<SetsListViewItemViewHolder> = mutableListOf()

    fun setOnRepsEntered(listener: (Int, String) -> Unit) {
        onRepsEntered = listener
        itemsFactory.setOnRepsEntered(listener)
    }

    fun setOnWeightEntered(listener: (Int, String) -> Unit) {
        onWeightEntered = listener
        itemsFactory.setOnWeightEntered(listener)
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

    fun submitSetsList(validateSetsList: List<ValidateSet>, exerciseType: ExerciseType) {
        val setsList = validateSetsList.map { it.setData }
        if (setsList.size - setsLayout.childCount >= 0) {
            if(currentExerciseType != exerciseType) {
                setsLayout.removeAllViews()
                holdersList.clear()
            }
            currentExerciseType = exerciseType
            addItems(setsList.size, setsList, exerciseType)
        } else{
            setsLayout.removeViewAt(setsLayout.childCount - 1)
            holdersList.removeLast()
        }
        validateItems(validateSetsList)
    }

    private fun addItems(itemsCount: Int, itemsData: List<ParameterizedSet>, itemsType: ExerciseType) {
        val viewsToAddCount = itemsCount - setsLayout.childCount
        var i = 0
        while (i < viewsToAddCount) {
            val itemIndex = setsLayout.childCount + 1
            val itemViewHolder = itemsFactory.getItem(itemsType)
            itemViewHolder.bindData(itemIndex, itemsData[itemIndex - 1])
            setsLayout.addView(itemViewHolder.getView())
            holdersList.add(itemViewHolder)
            i++
        }
    }

    private fun validateItems(validateSet: List<ValidateSet>) {
        for (i in validateSet.indices) {
            holdersList[i].validateItem(validateSet[i])
        }
    }
}