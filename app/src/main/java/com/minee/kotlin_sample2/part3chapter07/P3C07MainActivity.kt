package com.minee.kotlin_sample2.part3chapter07

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.minee.kotlin_sample2.R
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.util.MarkerIcons
import com.naver.maps.map.widget.LocationButtonView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class P3C07MainActivity : AppCompatActivity(), OnMapReadyCallback, Overlay.OnClickListener {

    private lateinit var naverMap: NaverMap
    private lateinit var locationSource: FusedLocationSource

    private val mapView: MapView by lazy {
        findViewById(R.id.mapView)
    }
    private val viewPager: ViewPager2 by lazy {
        findViewById(R.id.houseViewPager)
    }
    private val houseRecyclerView: RecyclerView by lazy {
        findViewById(R.id.houseRecyclerView)
    }
    private val currentLoacationButton: LocationButtonView by lazy {
        findViewById(R.id.currentLocationButton)
    }
    private val bottomSheetTitleTextView: TextView by lazy {
        findViewById(R.id.bottomSheetTitleTextView)
    }

    private val viewPagerAdapter = HouseViewPagerAdapter(itemClicked = {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "[지금 이 가격에 예약하세요!] ${it.title} ${it.price} 지도보기 ${it.imgUrl}")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent, null))
    })
    private val houseListAdapter = HouseListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p3_c07_main)

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        viewPager.adapter = viewPagerAdapter
        houseRecyclerView.adapter = houseListAdapter
        houseRecyclerView.layoutManager = LinearLayoutManager(this)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val selectedHouseModel = viewPagerAdapter.currentList[position]
                val cameraUpdate =
                    CameraUpdate.scrollTo(LatLng(selectedHouseModel.lat, selectedHouseModel.lng))
                        .animate(CameraAnimation.Easing)

                naverMap.moveCamera(cameraUpdate)
            }
        })
    }

    override fun onMapReady(map: NaverMap) {
        naverMap = map

        // 최대/최소 줌레벨 설정
        naverMap.maxZoom = 20.0
        naverMap.minZoom = 10.0

        // 위/경도 위치로 맵 이동
        naverMap.moveCamera(CameraUpdate.scrollTo(LatLng(37.497918, 127.027599)))


        // 현위치 버튼 활성화
        val uiSettings = naverMap.uiSettings
        uiSettings.isLocationButtonEnabled = false  // default 현위치 버튼을 비활성화 시킴
        currentLoacationButton.map = naverMap   // 현위치 버튼 위치 이동을 위해 새로운 버튼 추가

        locationSource =
            FusedLocationSource(this@P3C07MainActivity, LOCATION_PERMISSION_REQUEST_CODE)
        naverMap.locationSource = locationSource

        getHouseListFromApi()
    }

    private fun getHouseListFromApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(HouseService::class.java).also {
            it.getHouseList()
                .enqueue(object : Callback<HouseDto> {
                    override fun onResponse(call: Call<HouseDto>, response: Response<HouseDto>) {
                        if (response.isSuccessful.not()) {
                            return
                        }

                        response.body()?.let { dto ->
                            updateMarker(dto.items)
                            viewPagerAdapter.submitList(dto.items)
                            houseListAdapter.submitList(dto.items)
                            bottomSheetTitleTextView.text = "${dto.items.size}개의 숙소"
                        }
                    }

                    override fun onFailure(call: Call<HouseDto>, t: Throwable) {

                    }

                })
        }

    }

    private fun updateMarker(houseList: List<HouseModel>) {
        houseList.forEach { house ->
            val marker = Marker()
            marker.position = LatLng(house.lat, house.lng)
            marker.onClickListener = this
            marker.map = naverMap
            marker.tag = house.id
            marker.icon = MarkerIcons.BLACK
            marker.iconTintColor = Color.RED
        }
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

    override fun onClick(overlay: Overlay): Boolean {
        val selectedModel = viewPagerAdapter.currentList.firstOrNull {
            it.id == overlay.tag
        }
        selectedModel?.let {
            val position = viewPagerAdapter.currentList.indexOf(it)
            viewPager.currentItem = position
        }

        return true
    }
}