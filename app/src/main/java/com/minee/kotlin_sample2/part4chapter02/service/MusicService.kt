package com.minee.kotlin_sample2.part4chapter02.service

import retrofit2.Call
import retrofit2.http.GET

interface MusicService {
    @GET("/v3/15c14799-b80b-4713-be6e-17d6ea65ed67")
    fun listMusics() : Call<MusicDto>
}