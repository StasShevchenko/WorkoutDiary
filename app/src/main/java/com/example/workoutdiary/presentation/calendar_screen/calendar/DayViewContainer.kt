package com.example.workoutdiary.presentation.calendar_screen.calendar

import android.view.View
import android.widget.TextView
import com.example.workoutdiary.R
import com.kizitonwose.calendar.core.CalendarDay
import com.kizitonwose.calendar.view.ViewContainer

class DayViewContainer(view: View, onClickListener: (day: CalendarDay) -> Unit) : ViewContainer(view) {
    val textView = view.findViewById<TextView>(R.id.calendarDayText)
    val dotView = view.findViewById<View>(R.id.calendarDayDot)
    lateinit var day: CalendarDay
    init {
        view.setOnClickListener {
            onClickListener(day)
        }
    }

}