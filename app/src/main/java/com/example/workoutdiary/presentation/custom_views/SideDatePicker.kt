package com.example.workoutdiary.presentation.custom_views

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.os.ConfigurationCompat
import com.example.workoutdiary.R

import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class SideDatePicker(context: Context, attrs: AttributeSet) : RelativeLayout(context) {
    @RequiresApi(Build.VERSION_CODES.O)
    private val currentDate: LocalDateTime = LocalDateTime.now()
    @RequiresApi(Build.VERSION_CODES.O)
    private var date: LocalDateTime = LocalDateTime.now()
    private var onDateChanged: ((LocalDateTime) -> Unit)? = null

    init{
        initializeViews(context)
        updateText()
    }

    private fun initializeViews(context: Context) {
        val inflater: LayoutInflater =  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as (LayoutInflater)
        inflater.inflate(R.layout.sidedatepicker_view, this)
    }

    public fun setOnDateChangedListener(listener: (LocalDateTime) -> Unit){
        onDateChanged = listener
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        this.findViewById<AppCompatImageButton>(R.id.sidedatepicker_next).setOnClickListener {
            date = date.plusMonths(1)
            onDateChanged?.invoke(date)
            updateText()
        }
        this.findViewById<AppCompatImageButton>(R.id.sidedatepicker_previous).setOnClickListener {
        date = date.minusMonths(1)
            onDateChanged?.invoke(date)
            updateText()
        }
        this.findViewById<TextView>(R.id.sidedatepicker_view_current_value).setOnClickListener {
            date = LocalDateTime.now()
            onDateChanged?.invoke(date)
            updateText()
        }


    }

    private fun updateText(){
            this.findViewById<TextView>(R.id.sidedatepicker_view_current_value).text = date.month.getDisplayName(TextStyle.FULL_STANDALONE, ConfigurationCompat.getLocales(resources.configuration)[0])
                .replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                } + " "+ date.year
    }


}