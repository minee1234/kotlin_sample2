package com.minee.kotlin_sample2.part4chapter03


import com.google.gson.annotations.SerializedName

data class AddressInfo(
    val addressKey: String?,
    val addressType: String?,
    val adminDong: String?,
    val adminDongCode: String?,
    val adminDongCoord: AdminDongCoord?,
    val buildingIndex: String?,
    val buildingName: String?,
    val bunji: String?,
    @SerializedName("city_do")
    val cityDo: String?,
    @SerializedName("eup_myun")
    val eupMyun: String?,
    val fullAddress: String?,
    @SerializedName("gu_gun")
    val guGun: String?,
    val legalDong: String?,
    val legalDongCode: String?,
    val legalDongCoord: LegalDongCoord?,
    val mappingDistance: Int?,
    val ri: String?,
    val roadAddressKey: String?,
    val roadCode: String?,
    val roadCoord: RoadCoord?,
    val roadName: String?
)