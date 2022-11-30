package com.example.workoutdiary.presentation.calendar_screen

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.workoutdiary.R
import com.example.workoutdiary.databinding.CalendarScreenFragmentBinding
import com.example.workoutdiary.presentation.calendar_screen.calendar.DayViewContainer
import com.example.workoutdiary.presentation.calendar_screen.calendar.MonthViewContainer
import com.kizitonwose.calendar.core.*
import com.kizitonwose.calendar.view.MonthDayBinder
import com.kizitonwose.calendar.view.MonthHeaderFooterBinder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

@AndroidEntryPoint
class CalendarScreenFragment : Fragment(R.layout.calendar_screen_fragment) {
    private val viewModel: CalendarScreenViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = CalendarScreenFragmentBinding.bind(view)
        binding.apply {

            nextMonthButton.setOnClickListener {
                trainingsCalendar.findFirstVisibleMonth()?.let {
                    trainingsCalendar.scrollToMonth(it.yearMonth.nextMonth)
                }
            }
            previousMonthButton.setOnClickListener {
                trainingsCalendar.findFirstVisibleMonth()?.let {
                    trainingsCalendar.scrollToMonth(it.yearMonth.previousMonth)
                }
            }
            //calendar setup
            val currentMonth = YearMonth.now()
            val startMonth = currentMonth.minusMonths(120)
            val endMonth = currentMonth.plusMonths(120)
            val firstDayOfWeek = DayOfWeek.MONDAY
            val daysOfWeek = daysOfWeek(firstDayOfWeek = firstDayOfWeek)
            trainingsCalendar.setup(startMonth, endMonth, DayOfWeek.SUNDAY)
            trainingsCalendar.scrollToMonth(currentMonth)
            trainingsCalendar.dayBinder = object : MonthDayBinder<DayViewContainer> {
                override fun bind(container: DayViewContainer, data: CalendarDay) {
                    container.textView.text = data.date.dayOfMonth.toString()
                    container.day = data
                    container.textView.setTextColor(Color.BLACK)
                    container.textView.setBackgroundResource(R.color.white)
                    container.dotView.visibility = View.INVISIBLE
                    if (viewModel.trainingsList.value.isNotEmpty()) {
                        val index = data.date.dayOfMonth - 1
                        if (viewModel.currentMonth == data.date.month && viewModel.trainingsList.value[index] != null
                            && viewModel.trainingsList.value[index]!!.trainingDate.monthValue == data.date.monthValue) {
                            container.dotView.visibility = View.VISIBLE
                        }
                    }
                    if (data.date == viewModel.selectedDate) {
                        container.textView.setTextColor(resources.getColor(R.color.white))
                        container.textView.setBackgroundResource(R.drawable.selected_bg)
                        container.dotView.visibility = View.INVISIBLE
                    }
                    if (data.date == LocalDate.now()) {
                        container.textView.setTextColor(resources.getColor(R.color.white))
                        container.textView.setBackgroundResource(R.drawable.today_bg)
                        container.dotView.visibility = View.INVISIBLE
                    }
                    if (data.position != DayPosition.MonthDate) {
                        container.textView.setTextColor(Color.WHITE)
                        container.textView.background = null
                        container.dotView.visibility = View.INVISIBLE
                    }
                }

                override fun create(view: View): DayViewContainer {
                    return DayViewContainer(view) { day ->
                        viewModel.onEvent(CalendarScreenEvent.DaySelected(day))
                        binding.trainingsCalendar.notifyCalendarChanged()
                    }
                }
            }
            trainingsCalendar.monthScrollListener = {
                viewModel.onEvent(CalendarScreenEvent.MonthChanged(it.yearMonth.atStartOfMonth()))
            }
            trainingsCalendar.monthHeaderBinder =
                object : MonthHeaderFooterBinder<MonthViewContainer> {
                    override fun bind(container: MonthViewContainer, data: CalendarMonth) {
                        container.monthHeaderTextView.text =
                            data.yearMonth.month.toString().lowercase()
                                .replaceFirstChar { ch -> ch.uppercase() } + " " + data.yearMonth.year.toString()
                        container.titlesContainer.children.map { it as TextView }
                            .forEachIndexed { index, textView ->
                                val dayOfWeek = daysOfWeek[index]
                                val title =
                                    dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.getDefault())
                                textView.text = title
                            }
                    }

                    override fun create(view: View): MonthViewContainer {
                        return MonthViewContainer(view)
                    }
                }
                //end of calendar set up
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.trainingsList.collectLatest {
                    binding.trainingsCalendar.notifyCalendarChanged()
                }
            }
        }
    }
}