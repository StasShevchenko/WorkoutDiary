package com.example.workoutdiary.presentation.home_screen.chart_utils

import android.text.Spannable
import android.text.style.ForegroundColorSpan
import com.patrykandpatrick.vico.core.chart.values.ChartValues
import com.patrykandpatrick.vico.core.extension.appendCompat
import com.patrykandpatrick.vico.core.extension.transformToSpannable
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.marker.MarkerLabelFormatter

class DistanceMarkerLabelFormatter : MarkerLabelFormatter {
    override fun getLabel(
        markedEntries: List<Marker.EntryModel>,
        chartValues: ChartValues
    ): CharSequence {
        return markedEntries.transformToSpannable { model ->
            val distanceValue: String
            val kilometers = model.entry.y.toInt() / 1000
            val meters = model.entry.y.toInt() % 1000
            distanceValue = if (kilometers > 0) {
                "$kilometers,$meters"
            } else {
                meters.toString()
            }
            appendCompat(
                distanceValue,
                ForegroundColorSpan(model.color),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}