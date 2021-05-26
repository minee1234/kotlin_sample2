package com.minee.kotlin_sample2.part2chapter05

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TimePicker
import com.minee.kotlin_sample2.R
import java.util.*
import kotlin.concurrent.timer

class P2C05PhotoFrameActivity : AppCompatActivity() {
    private val photoList = mutableListOf<Uri>()

    private val frontImageView: ImageView by lazy {
        findViewById<ImageView>(R.id.frontImageView)
    }

    private val backgroundImageView: ImageView by lazy {
        findViewById<ImageView>(R.id.backgroundImageView)
    }

    private var currentImagePosition = 0

    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p2_c05_photo_frame)

        getPhotoUriFromIntent()
    }

    private fun getPhotoUriFromIntent() {
        val photoSize = intent.getIntExtra("photoListSize", 0)
        for (i in 0..photoSize) {
            intent.getStringExtra("photo$i")?.let {
                photoList.add(Uri.parse(it))
            }
        }
    }

    private fun startTimer() {
        timer = timer(period = 5 * 1000) {
            runOnUiThread{
                val current = currentImagePosition
                val next = if (photoList.size <= current + 1) 0 else current + 1

                backgroundImageView.setImageURI(photoList[current])

                frontImageView.alpha = 0f
                frontImageView.setImageURI(photoList[next])
                frontImageView.animate()
                    .alpha(1.0f)
                    .setDuration(1000)
                    .start()

                currentImagePosition = next
            }
        }
    }

    override fun onStop() {
        super.onStop()

        timer?.cancel()
        timer = null
    }

    override fun onStart() {
        super.onStart()

        startTimer()
    }

    override fun onDestroy() {
        super.onDestroy()

        timer?.cancel()
        timer = null
    }
}