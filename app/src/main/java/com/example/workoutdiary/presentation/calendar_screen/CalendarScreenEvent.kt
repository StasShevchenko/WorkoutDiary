package com.example.workoutdiary.presentation.calendar_screen

import com.kizitonwose.calendar.core.CalendarDay
import java.time.LocalDate

sealed class CalendarScreenEvent{
    data class DaySelected(val day: CalendarDay) : CalendarScreenEvent()
    data class MonthChanged(val date: LocalDate) : CalendarScreenEvent()
}
