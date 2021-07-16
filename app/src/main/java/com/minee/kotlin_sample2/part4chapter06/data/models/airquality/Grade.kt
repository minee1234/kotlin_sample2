package com.minee.kotlin_sample2.part4chapter06.data.models.airquality

import androidx.annotation.ColorRes
import com.google.gson.annotations.SerializedName
import com.minee.kotlin_sample2.R

enum class Grade(
    val label: String,
    val emoji: String,
    @ColorRes val colorResId: Int
) {
    @SerializedName("1")
    GOOD("좋음", "😄", R.color.blue_800),

    @SerializedName("2")
    NORMAL("보통", "😐", R.color.green_800),

    @SerializedName("3")
    BAD("나쁨", "😠", R.color.yellow_800),

    @SerializedName("4")
    AWFUL("매우 나쁨", "👿", R.color.red_800),

    UNKNOWN("미측정", "😮", R.color.gray_800);

    override fun toString(): String {
        return "$label $emoji"
    }
}