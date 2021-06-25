package com.minee.kotlin_sample2.part3chapter07

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.minee.kotlin_sample2.R
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons

class P3C07MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private val mapView: MapView by lazy {
        findViewById(R.id.mapView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p3_c07_main)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        // 최대/최소 줌레벨 설정
        naverMap.maxZoom = 20.0
        naverMap.minZoom = 10.0

        // 위/경도 위치로 맵 이동
        naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(37.497918, 127.027599)))


        val uiSettings = naverMap.uiSettings
        // 현위치 버튼 활성화
        uiSettings.isLocationButtonEnabled = true

        locationSource =
            FusedLocationSource(this@P3C07MainActivity, LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource

        // 핀 표시
        val marker = Marker()
        marker.position = LatLng(37.500560, 127.029779)
        marker.map = naverMap
        // 핀 컬러 변경
        marker.icon = MarkerIcons.BLACK
        marker.iconTintColor = Color.RED

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (locationSource.onRequestPermissionsResult(
                        requestCode,
                        permissions,
                        grantResults
                    )
                ) {
                    if (!locationSource.isActivated) {
                        naverMap.locationTrackingMode = LocationTrackingMode.None
                    }
                    return
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}