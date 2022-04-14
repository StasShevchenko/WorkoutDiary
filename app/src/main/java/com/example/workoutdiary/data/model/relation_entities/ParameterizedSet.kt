package com.example.workoutdiary.data.model.relation_entities


/*
I use this class to retrieve data
from both of OrderedSet and TrainingParameters
entities at the same time
 */
data class ParameterizedSet(
    val setOrder: Int,
    val repeats: Int? = null,
    val weight: Int? = null,
    val time: Int? = null,
    val distance: Int? = null
)
