package com.example.workoutdiary.presentation.calendar_screen.calendar

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.workoutdiary.R
import com.kizitonwose.calendar.view.ViewContainer

class MonthViewContainer(view: View) : ViewContainer(view) {
    val titlesContainer = view.findViewById<ViewGroup>(R.id.titles_container)
    val monthHeaderTextView = view.findViewById<TextView>(R.id.month_header_text_view)
}