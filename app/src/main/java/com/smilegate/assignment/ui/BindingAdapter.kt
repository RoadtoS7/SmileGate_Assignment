package com.smilegate.assignment.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.smilegate.assignment.R
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapter {
    @BindingAdapter("date")
    @JvmStatic
    fun setDate(textView: TextView, date: Date?) {
        if(date == null) return
        val formatStyle = textView.context.getString(R.string.date_format)
        val format = SimpleDateFormat(formatStyle, Locale.KOREA)
        val dateExp =  format.format(date)
        textView.text = dateExp
    }
}