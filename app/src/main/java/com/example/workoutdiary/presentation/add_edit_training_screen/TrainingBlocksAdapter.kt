package com.example.workoutdiary.presentation.add_edit_training_screen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.workoutdiary.data.model.relation_entities.ExerciseTrainingBlock
import com.example.workoutdiary.data.model.relation_entities.ParameterizedSet
import com.example.workoutdiary.databinding.TrainingDetailsItemBinding
import com.example.workoutdiary.utils.ExerciseType
import java.util.*
import kotlin.collections.ArrayList

class TrainingBlocksAdapter(
    private val onClickListener: OnTrainingBlockClickListener,
    private val onBlocksSwapListener: OnBlocksSwapListener,
    val list: ArrayList<Pair<ExerciseTrainingBlock, List<ParameterizedSet>>> = arrayListOf(),
    ) : RecyclerView.Adapter<ViewHolder>(),
    TrainingBlocksMoveCallback.ItemTouchHelperSwapCallback {

    private enum class ViewType {
        REPS_ITEM,
        WEIGHT_REPS_ITEM
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (ViewType.values()[viewType]) {
            ViewType.REPS_ITEM -> {
                val binding =
                    TrainingDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                 RepsViewHolder(binding)
            }
            ViewType.WEIGHT_REPS_ITEM ->{
                val binding =
                    TrainingDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                 WeightAndRepsViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (ViewType.values()[holder.itemViewType]) {
            ViewType.REPS_ITEM -> {
                val data = list[position]
                holder.itemView.setOnClickListener {
                    onClickListener.onTrainingBlockClick(
                        data.first.trainingBlockId,
                        data.first.trainingBlockOrder,
                        data.first.exerciseId
                    )
                }
                (holder as RepsViewHolder).bind(data)
            }
            ViewType.WEIGHT_REPS_ITEM -> {
                val data = list[position]
                holder.itemView.setOnClickListener {
                    onClickListener.onTrainingBlockClick(
                        data.first.trainingBlockId,
                        data.first.trainingBlockOrder,
                        data.first.exerciseId
                    )
                }
                (holder as WeightAndRepsViewHolder).bind(data)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        when (list[position].first.exerciseType) {
            ExerciseType.REPS -> return ViewType.REPS_ITEM.ordinal
            ExerciseType.WEIGHT_AND_REPS -> return ViewType.WEIGHT_REPS_ITEM.ordinal
            else -> return ViewType.REPS_ITEM.ordinal
        }
    }

    class DiffCallback(
        val oldList: List<Pair<ExerciseTrainingBlock, List<ParameterizedSet>>>,
        var newList: List<Pair<ExerciseTrainingBlock, List<ParameterizedSet>>>
    ) : DiffUtil.Callback(){
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem.first.trainingBlockId == newItem.first.trainingBlockId
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return (oldItem.first.exerciseName == newItem.first.exerciseName &&
                    oldItem.second == newItem.second)
        }
    }

    private val diffCallback = DiffCallback(list, ArrayList())

    interface OnBlocksSwapListener{
        fun onBlockSwapListener(
            fromPosition: Int,
            toPosition: Int
        )
    }

    interface OnTrainingBlockClickListener{
        fun onTrainingBlockClick(
            trainingBlockId: Int,
            trainingBlockOrder: Int,
            exerciseId: Int
        )
    }

    fun submitList(updatedList: List<Pair<ExerciseTrainingBlock, List<ParameterizedSet>>>) {
        diffCallback.newList = updatedList
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        list.clear()
        list.addAll(updatedList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(list, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
        onBlocksSwapListener.onBlockSwapListener(fromPosition, toPosition)
    }
}