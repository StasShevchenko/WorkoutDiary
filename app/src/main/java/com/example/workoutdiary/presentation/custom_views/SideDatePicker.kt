package com.example.workoutdiary.presentation.custom_views

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.os.ConfigurationCompat
import com.example.workoutdiary.R
import java.time.LocalDate

import java.time.LocalDateTime
import java.time.format.TextStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class SideDatePicker(context: Context, attrs: AttributeSet) : RelativeLayout(context, attrs) {
    @RequiresApi(Build.VERSION_CODES.O)
    private var date: LocalDate = LocalDate.now()
    private var onDateChanged: ((LocalDate) -> Unit)? = null
    private var onTextClickedAction: (() -> Unit)? = null

    init{
        initializeViews(context)
    }

    private fun initializeViews(context: Context) {
        val inflater: LayoutInflater =  context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)
                as (LayoutInflater)
        inflater.inflate(R.layout.sidedatepicker_view, this)
    }

     fun setOnDateChangedListener(listener: (LocalDate) -> Unit){
        onDateChanged = listener
    }
    fun setOnTextClickedAction(listener: () -> Unit) {
        onTextClickedAction = listener
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        updateText()
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
            date = LocalDate.now()
            onDateChanged?.invoke(date)
            updateText()
            onTextClickedAction?.invoke()
        }

    }

    @SuppressLint("SetTextI18n")
    private fun updateText(){
            this.findViewById<TextView>(R.id.sidedatepicker_view_current_value).text = date.month.getDisplayName(TextStyle.FULL_STANDALONE, ConfigurationCompat.getLocales(resources.configuration)[0])
                .replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                } + " "+ date.year
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState: SavedState = state as SavedState
        date = savedState.date
        updateText()
        super.onRestoreInstanceState(savedState.superState)
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState: Parcelable = super.onSaveInstanceState() as Parcelable

        val savedState = SavedState(superState)

        savedState.date = date
        return savedState
    }

   private class SavedState : BaseSavedState{
       var date: LocalDate = LocalDate.now()

        constructor(parcel: Parcel) : super(parcel){
            date = parcel.readValue(null) as LocalDate
        }

       constructor(parcelable: Parcelable?) : super(parcelable)

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeValue(date)
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }

    }
}