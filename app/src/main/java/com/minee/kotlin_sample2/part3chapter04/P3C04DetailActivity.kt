package com.minee.kotlin_sample2.part3chapter04

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.minee.kotlin_sample2.databinding.ActivityP3C04DetailBinding
import com.minee.kotlin_sample2.part3chapter04.model.Book
import com.minee.kotlin_sample2.part3chapter04.model.Review

class P3C04DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityP3C04DetailBinding
    private lateinit var db: P3C04Database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityP3C04DetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = getDatabase(this)

        val model = intent.getParcelableExtra<Book>("bookModel")

        binding.titleTextView.text = model?.title.orEmpty()
        binding.descriptionTextView.text = model?.description.orEmpty()

        Glide.with(this)
            .load(model?.coverSmallUrl.orEmpty())
            .into(binding.coverImageView)

        Thread {
            val review = db.reviewDao().getReview(model?.id?.toInt() ?: 0)
            runOnUiThread {
                binding.reviewEditText.setText(review?.review.orEmpty())
            }
        }.start()

        binding.saveButton.setOnClickListener {
            Thread {
                db.reviewDao().saveReview(
                    Review(
                        model?.id?.toInt() ?: 0,
                        binding.reviewEditText.text.toString()
                    )
                )
            }.start()
        }

    }
}