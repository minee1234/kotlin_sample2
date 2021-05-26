package com.minee.kotlin_sample2.part2chapter01

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.minee.kotlin_sample2.R

class P2C01MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p2_c01_main)

        val heightEditText = findViewById<EditText>(R.id.heightEditTextNumber)
        val weightEditText = findViewById<EditText>(R.id.weightEditTextNumber)
        val confirm = findViewById<Button>(R.id.confirmButton)

        confirm.setOnClickListener {
            Log.d("sample", "height: ${heightEditText.text}, weight: ${weightEditText.text}")

            if (heightEditText.text.isEmpty() || weightEditText.text.isEmpty()) {
                Toast.makeText(this, "빈 값이 있습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val height = heightEditText.text.toString().toInt()
            val weight = weightEditText.text.toString().toInt()

            val intent = Intent(this, P2C01ResultActivity::class.java)

            intent.putExtra("height", height)
            intent.putExtra("weight", weight)

            startActivity(intent)
        }
    }
}