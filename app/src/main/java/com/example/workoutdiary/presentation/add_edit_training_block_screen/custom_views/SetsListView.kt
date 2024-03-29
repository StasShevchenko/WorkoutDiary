package com.example.workoutdiary.presentation.add_edit_training_block_screen.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.workoutdiary.R
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import com.example.workoutdiary.presentation.add_edit_training_block_screen.ValidateSet
import com.example.workoutdiary.utils.ExerciseType


class SetsListView(context: Context, attrs: AttributeSet) : LinearLayout(context, attrs) {
    private var currentExerciseType: ExerciseType? = null
    private lateinit var setsLayout: LinearLayout

    private val itemsFactory: SetsListViewItemsFactory = SetsListViewItemsFactory(
        context,
        this
    )

    private val holdersList: MutableList<SetsListViewItemViewHolder> = mutableListOf()

    fun setOnRepsEntered(listener: (Int, String) -> Unit) {
        itemsFactory.setOnRepsEntered(listener)
    }

    fun setOnWeightEntered(listener: (Int, String) -> Unit) {
        itemsFactory.setOnWeightEntered(listener)
    }

    fun setOnTimeEntered(listener: (Int, String) -> Unit) {
        itemsFactory.setOnTimeEntered(listener)
    }

    fun setOnDistanceEntered(listener: (Int, String) -> Unit) {
        itemsFactory.setOnDistanceEntered(listener)
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
        } else if((setsLayout.childCount - setsList.size) == 1){
            setsLayout.removeViewAt(setsLayout.childCount - 1)
            holdersList.removeLast()
        } else{
            setsLayout.removeAllViews()
            holdersList.clear()
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