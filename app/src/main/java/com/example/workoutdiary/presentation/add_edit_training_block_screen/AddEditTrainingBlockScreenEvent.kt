package com.example.workoutdiary.presentation.add_edit_training_block_screen

import com.example.workoutdiary.data.model.entities.Exercise
import com.example.workoutdiary.data.model.entities.Muscle

sealed class AddEditTrainingBlockScreenEvent {
    class MuscleSelected(val muscle: Muscle) : AddEditTrainingBlockScreenEvent()
    class ExerciseSelected(val exercise: Exercise) : AddEditTrainingBlockScreenEvent()
    class RepsEntered(val index: Int, val value: String) : AddEditTrainingBlockScreenEvent()
    class WeightEntered(val index: Int, val value: String) : AddEditTrainingBlockScreenEvent()
    class TimeEntered(val index: Int, val value: String) : AddEditTrainingBlockScreenEvent()

    class DistanceEntered(val index: Int, val value: String): AddEditTrainingBlockScreenEvent()
    class MuscleEntered(val muscleName: String) : AddEditTrainingBlockScreenEvent()
    class ExerciseEntered(val exerciseName: String) : AddEditTrainingBlockScreenEvent()
    object SetNumberIncreased : AddEditTrainingBlockScreenEvent()
    object SetNumberDecreased : AddEditTrainingBlockScreenEvent()
    object SaveChosen : AddEditTrainingBlockScreenEvent()
    object DeleteChosen : AddEditTrainingBlockScreenEvent()
}
