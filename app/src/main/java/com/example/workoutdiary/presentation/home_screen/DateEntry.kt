package com.example.workoutdiary.presentation.home_screen

import com.patrykandpatrick.vico.core.entry.ChartEntry

class DateEntry(
    val date: String,
    override val x: Float,
    override val y: Float,
) : ChartEntry {
    override fun withY(y: Float) = DateEntry(
        date = this.date,
        x = this.x,
        y = y
    )
}