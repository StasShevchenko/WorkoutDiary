package com.example.workoutdiary.presentation.calendar_screen

import android.app.ActionBar.LayoutParams
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.workoutdiary.R
import com.example.workoutdiary.databinding.CalendarScreenFragmentBinding
import com.example.workoutdiary.presentation.MainActivity
import com.example.workoutdiary.presentation.calendar_screen.calendar.DayViewContainer
import com.example.workoutdiary.presentation.calendar_screen.calendar.MonthViewContainer
import com.example.workoutdiary.presentation.utils.FabButtonClick
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
class CalendarScreenFragment : Fragment(R.layout.calendar_screen_fragment), FabButtonClick {
    private val viewModel: CalendarScreenViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = CalendarScreenFragmentBinding.bind(view)
        val activity = activity as MainActivity
        activity.setFabListener(this)
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
            trainingsCalendar.scrollToMonth(viewModel.currentMonth)
            trainingsCalendar.dayBinder = object : MonthDayBinder<DayViewContainer> {
                override fun bind(container: DayViewContainer, data: CalendarDay) {
                    container.textView.text = data.date.dayOfMonth.toString()
                    container.day = data
                    container.textView.setTextColor(Color.BLACK)
                    container.textView.setBackgroundResource(R.color.white)
                    container.dotView.visibility = View.INVISIBLE
                    if (viewModel.trainingsList.value.isNotEmpty()) {
                        val index = data.date.dayOfMonth - 1
                        if (viewModel.currentMonth.month == data.date.month && viewModel.trainingsList.value[index] != null
                            && viewModel.trainingsList.value[index]!!.trainingDate.monthValue == data.date.monthValue
                        ) {
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
                        val monthViewContainer = MonthViewContainer(view)
                        monthViewContainer.monthHeaderTextView.setOnClickListener {
                            trainingsCalendar.scrollToMonth(YearMonth.now())
                        }
                        return monthViewContainer
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentTraining.collectLatest { currentTraining ->
                    viewModel.currentTrainingDetails.collectLatest { currentTrainingDetails ->
                        binding.trainingDetailsContainer.removeAllViews()
                        binding.trainingDetailsContainer.setOnClickListener(null)
                        if (currentTraining != null && currentTrainingDetails.isNotEmpty()) {
                            val trainingNameTextView = TextView(requireContext())
                            trainingNameTextView.text =
                                "Имя тренировки: " + currentTraining.trainingName
                            trainingNameTextView.gravity = Gravity.START
                            trainingNameTextView.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Title)
                            val exercisesHeaderTextView = TextView(requireContext())
                            exercisesHeaderTextView.text = "Упражнения:"
                            exercisesHeaderTextView.gravity = Gravity.START
                            exercisesHeaderTextView.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Title)
                            binding.trainingDetailsContainer.addView(trainingNameTextView)
                            binding.trainingDetailsContainer.addView(exercisesHeaderTextView)
                            binding.trainingDetailsContainer.setOnClickListener{
                                val action = CalendarScreenFragmentDirections
                                    .actionCalendarScreenFragmentToAddEditTrainingScreenFragment(
                                        trainingDate = currentTraining.trainingDate,
                                        trainingId = currentTraining.trainingId,
                                        trainingName = currentTraining.trainingName
                                    )
                                findNavController().navigate(action)
                            }
                            var i = 1
                            currentTrainingDetails.keys.forEach { trainingBlock ->
                                val trainingBlockNameTextView = TextView(requireContext())
                                trainingBlockNameTextView.text = i.toString() + " " + trainingBlock.exerciseName
                                trainingBlockNameTextView.gravity = Gravity.START
                                val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                                params.setMargins(0, 0, 0, 8);
                                trainingBlockNameTextView.layoutParams = params
                                binding.trainingDetailsContainer.addView(trainingBlockNameTextView)
                                i++
                            }
                        } else {
                            val emptyMessageTextView = TextView(requireContext())
                            emptyMessageTextView.text = "Тренировки нет"
                            emptyMessageTextView.gravity = Gravity.START
                            emptyMessageTextView.setTextAppearance(androidx.appcompat.R.style.TextAppearance_AppCompat_Title)
                            binding.trainingDetailsContainer.addView(emptyMessageTextView)
                        }
                    }
                }
            }
        }
    }

    override fun onFabClicked() {
        val action = CalendarScreenFragmentDirections.actionCalendarScreenFragmentToAddEditTrainingScreenFragment(
            trainingDate = viewModel.currentTraining.value?.trainingDate ?: viewModel.selectedDate,
            trainingId = viewModel.currentTraining.value?.trainingId ?: -1,
            trainingName = viewModel.currentTraining.value?.trainingName ?: ""
        )
        findNavController().navigate(action)
    }
}