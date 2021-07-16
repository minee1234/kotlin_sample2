package com.minee.kotlin_sample2.part4chapter06.data.models.tmcoordinates


import com.google.gson.annotations.SerializedName

data class TmCoordinatesResponse(
    val documents: List<Document>?,
    val meta: Meta?
)