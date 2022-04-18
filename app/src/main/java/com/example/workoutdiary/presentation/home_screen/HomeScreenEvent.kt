package com.example.workoutdiary.presentation.home_screen

import java.time.LocalDate

sealed class HomeScreenEvent {
    data class ChangeDate(val date: LocalDate) : HomeScreenEvent()
}