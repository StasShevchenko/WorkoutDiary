package com.example.workoutdiary.presentation.add_edit_training_screen

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class TrainingBlocksMoveCallback(
    private val swapCallback: ItemTouchHelperSwapCallback,
    private val swapFinishedCallback: ItemTouchHelperSwapFinishedCallback
) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlags = ItemTouchHelper.UP + ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }


    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        swapCallback.onRowMoved(viewHolder.absoluteAdapterPosition, target.absoluteAdapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        return
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        swapFinishedCallback.rowSwapFinished()
    }

    interface ItemTouchHelperSwapFinishedCallback{
        fun rowSwapFinished()
    }

    interface ItemTouchHelperSwapCallback{
        fun onRowMoved(fromPosition: Int, toPosition: Int)
    }
}