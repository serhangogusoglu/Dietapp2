package com.example.diet_app

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

interface DateSelectionListener {
    fun onDateSelected(dateString: String)
}

class DatePickerFragment : DialogFragment() {

    var dateSelectionListener: DateSelectionListener? = null

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy" , Locale.getDefault())

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        val constraintBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointBackward.now())

        val today = MaterialDatePicker.todayInUtcMilliseconds()

        val datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Birth Date")
            .setSelection(today)
            .setCalendarConstraints(constraintBuilder.build())
            .build()


        datePicker.addOnPositiveButtonClickListener { selection ->
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = selection

            val dateString = dateFormat.format(calendar.time)

            dateSelectionListener?.onDateSelected(dateString)
        }

        return datePicker.onCreateDialog(savedInstanceState)
    }

    companion object {
        fun show(fragment: Fragment, listener: DateSelectionListener) {
            val constraintsBuilder = CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now())

            val today = MaterialDatePicker.todayInUtcMilliseconds()

            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Birth Date")
                .setSelection(today)
                .setCalendarConstraints(constraintsBuilder.build())
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
                calendar.timeInMillis = selection

                // Seçilen tarihi formatla (dd/MM/yyyy)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val dateString = dateFormat.format(calendar.time)

                listener.onDateSelected(dateString)
            }

            datePicker.show(fragment.childFragmentManager, "BIRTH_DATE_PICKER")
        }
    }
}