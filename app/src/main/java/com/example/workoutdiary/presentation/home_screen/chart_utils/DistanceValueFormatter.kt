package com.example.workoutdiary.presentation.home_screen.chart_utils

import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.values.ChartValues

class DistanceValueFormatter : AxisValueFormatter<AxisPosition.Vertical.Start> {
    override fun formatValue(value: Float, chartValues: ChartValues): CharSequence {
        val distanceValue: String
        val kilometers = value.toInt() / 1000
        val meters = value.toInt() % 1000
        distanceValue = if (kilometers > 0) {
            "$kilometers,$meters"
        } else {
            meters.toString()
        }
        return distanceValue
    }
}