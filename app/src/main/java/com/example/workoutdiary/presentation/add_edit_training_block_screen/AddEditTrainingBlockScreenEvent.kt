package com.example.workoutdiary.presentation.add_edit_training_block_screen

import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.data.model.entities.Muscle

sealed class AddEditTrainingBlockScreenEvent {
    class MuscleSelected(val muscle: Muscle): AddEditTrainingBlockScreenEvent()
    class ExerciseSelected(val exercise: Exercise): AddEditTrainingBlockScreenEvent()
    object SetNumberIncreased : AddEditTrainingBlockScreenEvent()
    object SetNumberDecreased : AddEditTrainingBlockScreenEvent()
}
