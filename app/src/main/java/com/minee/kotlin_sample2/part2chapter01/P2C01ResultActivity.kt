package com.minee.kotlin_sample2.part2chapter01

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.TextValueSanitizer
import android.util.Log
import android.widget.TextView
import com.minee.kotlin_sample2.R
import kotlin.math.pow

class P2C01ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p2_c01_result)

        val height = intent.getIntExtra("height", 0)
        val weight = intent.getIntExtra("weight", 0)

        Log.d("sample", "height: $height, weight: $weight")

        val bmi = weight / (height / 100.0).pow(2.0)
        val resultText = when {
            bmi >= 35.0 -> "고도 미만"
            bmi >= 30.0 -> "중정도 미만"
            bmi >= 25.0 -> "경도 미만"
            bmi >= 23.0 -> "과체중"
            bmi >= 18.5 -> "정상체중"
            else -> "저체중"
        }

        val bmiResultTextView = findViewById<TextView>(R.id.bmiResultTextView)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)

        bmiResultTextView.text = bmi.toString()
        resultTextView.text = resultText

    }
}