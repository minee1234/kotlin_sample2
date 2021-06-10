package com.minee.kotlin_sample2.part3chapter04.api

import com.minee.kotlin_sample2.part3chapter04.model.BestSellerDto
import com.minee.kotlin_sample2.part3chapter04.model.SearchBookDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BookService {
    @GET("/api/search.api?output=json")
    fun getBooksByName(
        @Query("key") apiKey: String,
        @Query("query") keyWord: String
    ): Call<SearchBookDto>

    @GET("/api/bestSeller.api?categoryId=100&output=json")
    fun getBestSellerBooks(
        @Query("key") apiKey: String
    ): Call<BestSellerDto>
}