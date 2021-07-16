package com.minee.kotlin_sample2.part4chapter03

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.minee.kotlin_sample2.R
import com.minee.kotlin_sample2.databinding.ActivityP4C03MapBinding
import com.minee.kotlin_sample2.part4chapter03.model.SearchResultEntity

class P4C03MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityP4C03MapBinding
    private lateinit var locationManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityP4C03MapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupGoogleMap()
        bindViews()
    }

    private fun bindViews() = with(binding) {
        currentLocationButton.setOnClickListener {
            getMyLocation()
        }
    }

    private fun getMyLocation() {
        if (::locationManager.isInitialized.not()) {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (isGpsEnabled) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {

                }

            }

        }
    }

    private fun setupGoogleMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        Log.d("minee", "onMapReady -------------------------")
        val markerInfo = intent.getParcelableExtra<SearchResultEntity>(EXTRA_SEARCH_RESULT)
        val lat = markerInfo?.locationLatLng?.latitude?.toDouble() ?: 0.0
        val lng = markerInfo?.locationLatLng?.longitude?.toDouble() ?: 0.0
        val name = markerInfo?.name ?: "Unknown"
        val fullAddress = markerInfo?.fullAddress ?: "Unknown"

        map.addMarker(
            MarkerOptions()
                .position(LatLng(lat, lng))
                .title(name)
                .snippet(fullAddress)
        )

        map.moveCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(lat, lng),
                CAMERA_ZOOM_LEVEL
            )
        )
    }

    companion object {
        const val EXTRA_SEARCH_RESULT = "search_result_entity"
        const val CAMERA_ZOOM_LEVEL = 17.0f
    }
}