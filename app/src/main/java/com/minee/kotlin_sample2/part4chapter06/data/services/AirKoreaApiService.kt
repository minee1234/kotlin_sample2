package com.minee.kotlin_sample2.part4chapter06.data.services

import com.minee.kotlin_sample2.BuildConfig
import com.minee.kotlin_sample2.part4chapter06.data.models.airquality.AirQualityResponse
import com.minee.kotlin_sample2.part4chapter06.data.models.monitoringstation.MonitoringStationsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AirKoreaApiService {

    @GET(
        "B552584/MsrstnInfoInqireSvc/getNearbyMsrstnList" +
                "?serviceKey=${BuildConfig.AIRKOREA_API_KEY}" +
                "&returnType=json"
    )
    suspend fun getNearbyMonitoringStation(
        @Query("tmX") tmX: Double,
        @Query("tmY") tmY: Double
    ): Response<MonitoringStationsResponse>

    @GET(
        "B552584/ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty" +
                "?serviceKey=${BuildConfig.AIRKOREA_API_KEY}" +
                "&returnType=json" +
                "&dataTerm=DAILY" +
                "&ver=1.3"
    )
    suspend fun getRealTimeAirQualities(
        @Query("stationName") stationName: String,
    ): Response<AirQualityResponse>
}