package com.example.workoutdiary.presentation.home_screen.chart_utils

import android.text.Spannable
import android.text.style.ForegroundColorSpan
import com.patrykandpatrick.vico.core.extension.appendCompat
import com.patrykandpatrick.vico.core.extension.transformToSpannable
import com.patrykandpatrick.vico.core.marker.Marker
import com.patrykandpatrick.vico.core.marker.MarkerLabelFormatter

class TimeMarkerLabelFormatter : MarkerLabelFormatter {
    override fun getLabel(markedEntries: List<Marker.EntryModel>): CharSequence {
        return markedEntries.transformToSpannable{model ->
            val timeValue: String
            val minutes = model.entry.y.toInt() / 60
            val seconds = model.entry.y.toInt() % 60
            val leadingZero = if(seconds < 10) "0" else ""
            timeValue = "${minutes}:${leadingZero}${seconds}"
            appendCompat(
                timeValue,
                ForegroundColorSpan(model.color),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}