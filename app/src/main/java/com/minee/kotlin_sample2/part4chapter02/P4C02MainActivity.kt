package com.minee.kotlin_sample2.part4chapter02

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.minee.kotlin_sample2.R

class P4C02MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p4_c02_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, AudioPlayerFragment.newInstance())
            .commit()
    }
}