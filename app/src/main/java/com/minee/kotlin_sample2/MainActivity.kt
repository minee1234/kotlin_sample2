package com.minee.kotlin_sample2

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.minee.kotlin_sample2.part2chapter01.P2C01MainActivity
import com.minee.kotlin_sample2.part2chapter02.P2C02MainActivity
import com.minee.kotlin_sample2.part2chapter03.P2C03MainActivity
import com.minee.kotlin_sample2.part2chapter04.P2C04MainActivity
import com.minee.kotlin_sample2.part2chapter05.P2C05MainActivity
import com.minee.kotlin_sample2.part2chapter06.P2C06MainActivity
import com.minee.kotlin_sample2.part2chapter07.P2C07MainActivity
import com.minee.kotlin_sample2.part2chapter08.P2C08MainActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onClick(v: View) {
        when (v.id) {
            R.id.btn_p2_c01 -> startActivity(Intent(this, P2C01MainActivity::class.java))
            R.id.btn_p2_c02 -> startActivity(Intent(this, P2C02MainActivity::class.java))
            R.id.btn_p2_c03 -> startActivity(Intent(this, P2C03MainActivity::class.java))
            R.id.btn_p2_c04 -> startActivity(Intent(this, P2C04MainActivity::class.java))
            R.id.btn_p2_c05 -> startActivity(Intent(this, P2C05MainActivity::class.java))
            R.id.btn_p2_c06 -> startActivity(Intent(this, P2C06MainActivity::class.java))
            R.id.btn_p2_c07 -> startActivity(Intent(this, P2C07MainActivity::class.java))
            R.id.btn_p2_c08 -> startActivity(Intent(this, P2C08MainActivity::class.java))
        }
    }
}