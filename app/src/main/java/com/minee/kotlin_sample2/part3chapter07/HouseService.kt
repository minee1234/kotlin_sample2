package com.minee.kotlin_sample2.part3chapter07

import retrofit2.Call
import retrofit2.http.GET

interface HouseService {
    @GET("/v3/665321fd-f195-4987-a0b3-e8bb3d527fd9")
    fun getHouseList(): Call<HouseDto>
}