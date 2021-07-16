package com.minee.kotlin_sample2.part4chapter06.data

import com.minee.kotlin_sample2.BuildConfig
import com.minee.kotlin_sample2.part4chapter06.data.models.airquality.MeasuredValue
import com.minee.kotlin_sample2.part4chapter06.data.models.monitoringstation.MonitoringStation
import com.minee.kotlin_sample2.part4chapter06.data.services.AirKoreaApiService
import com.minee.kotlin_sample2.part4chapter06.data.services.KakaoLocalApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object Repository {

    suspend fun getNearbyMonitoringStation(
        latitude: Double,
        longitude: Double
    ): MonitoringStation? {
        val tmCoordinates = kakaoLocalApiService
            .getTmCoordinates(longitude, latitude)
            .body()
            ?.documents
            ?.firstOrNull()

        val tmX = tmCoordinates?.x
        val tmY = tmCoordinates?.y

        return airKoreaApiService
            .getNearbyMonitoringStation(tmX!!, tmY!!)
            .body()
            ?.response
            ?.body
            ?.items
            ?.minByOrNull {
                it.tm ?: Double.MAX_VALUE
            }
    }

    suspend fun getLatestAirQualityData(stationName: String): MeasuredValue? =
        airKoreaApiService
            .getRealTimeAirQualities(stationName)
            .body()
            ?.response
            ?.body
            ?.measuredValues
            ?.firstOrNull()

    private val kakaoLocalApiService: KakaoLocalApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Url.KAKAO_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClient())
            .build()
            .create()
    }

    private val airKoreaApiService: AirKoreaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Url.AIRKOREA_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(buildHttpClient())
            .build()
            .create()
    }

    private fun buildHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()


}