package com.example.workoutdiary.presentation.home_screen.chart_utils

import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.values.ChartValues

class TimeValueFormatter : AxisValueFormatter<AxisPosition.Vertical.Start> {
    override fun formatValue(value: Float, chartValues: ChartValues): CharSequence {
        val timeValue: String
        val minutes = value.toInt() / 60
        val seconds = value.toInt() % 60
        val leadingZero = if(seconds < 10) "0" else ""
        timeValue = "${minutes}:${leadingZero}${seconds}"
        return timeValue
    }
}