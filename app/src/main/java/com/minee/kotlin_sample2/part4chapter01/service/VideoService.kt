package com.minee.kotlin_sample2.part4chapter01.service

import com.minee.kotlin_sample2.part4chapter01.dto.VideoDto
import retrofit2.Call
import retrofit2.http.GET

interface VideoService {
    @GET("/v3/5fb5a82f-370c-4125-ac23-b0e46aecdd97")
    fun listVideos(): Call<VideoDto>
}