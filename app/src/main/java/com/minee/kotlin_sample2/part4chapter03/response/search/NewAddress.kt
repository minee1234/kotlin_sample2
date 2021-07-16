package com.minee.kotlin_sample2.part4chapter03.response.search

data class NewAddress(
    val centerLat: String,
    val centerLon: String,
    val frontLat: String,
    val frontLon: String,
    val roadName: String,
    val bldNo1: String,
    val bldNo2: String,
    val roadId: String,
    val fullAddressRoad: String
)
