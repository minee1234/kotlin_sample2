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
import com.minee.kotlin_sample2.part3chapter01.P3C01MainActivity
import com.minee.kotlin_sample2.part3chapter02.P3C02MainActivity
import com.minee.kotlin_sample2.part3chapter03.P3C03MainActivity
import com.minee.kotlin_sample2.part3chapter04.P3C04MainActivity
import com.minee.kotlin_sample2.part3chapter05.P3C05MainActivity
import com.minee.kotlin_sample2.part3chapter06.P3C06MainActivity
import com.minee.kotlin_sample2.part3chapter07.P3C07MainActivity
import com.minee.kotlin_sample2.part4chapter01.P4C01MainActivity
import com.minee.kotlin_sample2.part4chapter02.P4C02MainActivity

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
            R.id.btn_p3_c01 -> startActivity(Intent(this, P3C01MainActivity::class.java))
            R.id.btn_p3_c02 -> startActivity(Intent(this, P3C02MainActivity::class.java))
            R.id.btn_p3_c03 -> startActivity(Intent(this, P3C03MainActivity::class.java))
            R.id.btn_p3_c04 -> startActivity(Intent(this, P3C04MainActivity::class.java))
            R.id.btn_p3_c05 -> startActivity(Intent(this, P3C05MainActivity::class.java))
            R.id.btn_p3_c06 -> startActivity(Intent(this, P3C06MainActivity::class.java))
            R.id.btn_p3_c07 -> startActivity(Intent(this, P3C07MainActivity::class.java))
            R.id.btn_p4_c01 -> startActivity(Intent(this, P4C01MainActivity::class.java))
            R.id.btn_p4_c02 -> startActivity(Intent(this, P4C02MainActivity::class.java))
        }
    }
}