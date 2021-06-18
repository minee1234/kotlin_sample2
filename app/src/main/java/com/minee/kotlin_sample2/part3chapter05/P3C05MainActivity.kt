package com.minee.kotlin_sample2.part3chapter05

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.minee.kotlin_sample2.R

class P3C05MainActivity : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p3_c05_main)
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser == null) {
            startActivity(Intent(this, P3C05LoginActivity::class.java))
        } else {
            startActivity(Intent(this, P3C05LikeActivity::class.java))
            finish()
        }
    }
}