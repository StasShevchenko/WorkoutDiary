package com.example.workoutdiary.data.model.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class OrderedSet(
    @PrimaryKey(autoGenerate = true)
    val orderedSetId: Long,
    val setOrder: Int,
    val trainingBlockId: Int,
    val trainingParametersId: Int
)
