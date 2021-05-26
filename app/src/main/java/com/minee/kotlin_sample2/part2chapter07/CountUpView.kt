package com.minee.kotlin_sample2.part2chapter07

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class CountUpView(context: Context, attrs: AttributeSet? = null) :
    AppCompatTextView(context, attrs) {

    private var startTimeStamp = 0L

    val countUpAction: Runnable = object : Runnable {
        override fun run() {
            val currentTimeStamp = SystemClock.elapsedRealtime()
            val countTimeSeconds = (currentTimeStamp - startTimeStamp) / 1000L

            updateTimeTextView(countTimeSeconds.toInt())

            handler?.postDelayed(this, 1000L)
        }
    }

    fun startCountUp() {
        startTimeStamp = SystemClock.elapsedRealtime()
        handler?.post(countUpAction)
    }

    fun stopCountUp() {
        handler?.removeCallbacks(countUpAction)
    }

    fun updateTimeTextView(countTimeSeconds: Int) {
        val minutes = countTimeSeconds / 60
        val seconds = countTimeSeconds % 60

        text = "%02d:%02d".format(minutes, seconds)
    }

    fun clearCountTime() {
        updateTimeTextView(0)
    }
}