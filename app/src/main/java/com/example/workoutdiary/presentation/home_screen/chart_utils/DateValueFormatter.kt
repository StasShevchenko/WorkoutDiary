package com.example.workoutdiary.presentation.home_screen.chart_utils

import com.example.workoutdiary.presentation.home_screen.DateEntry
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.values.ChartValues

class DateValueFormatter : AxisValueFormatter<AxisPosition.Horizontal.Bottom> {
    override fun formatValue(value: Float, chartValues: ChartValues): CharSequence {
        var result = ""
        val chartNullableValues = chartValues.chartEntryModel.entries
        result =
            (chartNullableValues.getOrNull(0)?.getOrNull(value.toInt()) as DateEntry?)?.date
                ?: ""
        return result
    }
}