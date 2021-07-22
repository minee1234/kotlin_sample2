package com.minee.kotlin_sample2.part4chapter06

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.minee.kotlin_sample2.databinding.ActivityP4C06MainBinding
import com.minee.kotlin_sample2.part4chapter06.data.Repository
import com.minee.kotlin_sample2.part4chapter06.data.models.airquality.Grade
import com.minee.kotlin_sample2.part4chapter06.data.models.airquality.MeasuredValue
import com.minee.kotlin_sample2.part4chapter06.data.models.monitoringstation.MonitoringStation
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class P4C06MainActivity : AppCompatActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var cancellationTokenSource: CancellationTokenSource? = null

    private val binding by lazy { ActivityP4C06MainBinding.inflate(layoutInflater) }
    private val scope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bindViews()
        initVariables()
        requestLocationPermissions()
    }

    private fun bindViews() {
        binding.refreshLayout.setOnRefreshListener {
            fetchAirQualityData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancellationTokenSource?.cancel()
        scope.cancel()
    }

    private fun initVariables() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun requestLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            REQUEST_ACCESS_LOCATION_PERMISSIONS
        )
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestBackgroundLocationPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ),
            REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS
        )
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val locationPermissionGranted =
            requestCode == REQUEST_ACCESS_LOCATION_PERMISSIONS &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED

        val backgroundLocationPermissionGranted =
            requestCode == REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!backgroundLocationPermissionGranted) {
                requestBackgroundLocationPermissions()
            } else {
                fetchAirQualityData()
            }
        } else {
            if (!locationPermissionGranted) {
                finish()
            } else {
                fetchAirQualityData()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun fetchAirQualityData() {
        cancellationTokenSource = CancellationTokenSource()

        fusedLocationProviderClient
            .getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource!!.token
            ).addOnSuccessListener { location ->
                scope.launch {
                    binding.errorDescriptionTextView.isVisible = false
                    try {
                        val monitoringStation =
                            Repository.getNearbyMonitoringStation(
                                location.latitude,
                                location.longitude
                            )
                        val measuredValue =
                            Repository.getLatestAirQualityData(monitoringStation!!.stationName!!)

                        displayAirQualityData(monitoringStation!!, measuredValue!!)
                    } catch (e: Exception) {
                        binding.errorDescriptionTextView.isVisible = true
                        binding.contentsLayout.alpha = 0F
                    } finally {
                        binding.progressBar.isVisible = false
                        binding.refreshLayout.isRefreshing = false
                    }

                }
            }
    }

    @SuppressLint("SetTextI18n")
    private fun displayAirQualityData(
        monitoringStation: MonitoringStation,
        measuredValue: MeasuredValue
    ) {
        with(binding) {
            contentsLayout.animate()
                .alpha(1F)
                .start()

            measuringStationNameTextView.text = monitoringStation.stationName
            measuringStationAddressTextView.text = monitoringStation.addr

            (measuredValue.khaiGrade ?: Grade.UNKNOWN).let { grade ->
                root.setBackgroundResource(grade.colorResId)
                totalGradeLabelTextView.text = grade.label
                totalGradeEmojiTextView.text = grade.emoji
            }

            with(measuredValue) {
                fineDustInformationTextView.text =
                    "미세먼지: $pm10Value ㎍/㎥ ${(pm10Grade ?: Grade.UNKNOWN).emoji}"
                ultraFineDustInformationTextView.text =
                    "초미세먼지: $pm25Value ㎍/㎥ ${(pm25Grade ?: Grade.UNKNOWN).emoji}"

                with(binding.so2Item) {
                    labelTextView.text = "아황산가스"
                    gradeTextView.text = (so2Grade ?: Grade.UNKNOWN).toString()
                    valueTextView.text = "$so2Value ppm"
                }
                with(binding.coItem) {
                    labelTextView.text = "일산화탄소"
                    gradeTextView.text = (coGrade ?: Grade.UNKNOWN).toString()
                    valueTextView.text = "$coValue ppm"
                }
                with(binding.o3Item) {
                    labelTextView.text = "오존"
                    gradeTextView.text = (o3Grade ?: Grade.UNKNOWN).toString()
                    valueTextView.text = "$o3Value ppm"
                }
                with(binding.no2Item) {
                    labelTextView.text = "이산화질소"
                    gradeTextView.text = (no2Grade ?: Grade.UNKNOWN).toString()
                    valueTextView.text = "$no2Value ppm"
                }
            }
        }
    }

    companion object {
        private const val REQUEST_ACCESS_LOCATION_PERMISSIONS = 100
        private const val REQUEST_BACKGROUND_ACCESS_LOCATION_PERMISSIONS = 101
    }
}