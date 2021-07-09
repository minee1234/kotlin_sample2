package com.minee.kotlin_sample2.part4chapter02

data class MusicModel(
    val id: Long,
    val track: String,
    val streamUrl: String,
    val artist: String,
    val coverUrl: String,
    val isPlaying: Boolean = false
)
