package com.minee.kotlin_sample2.part4chapter03

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.minee.kotlin_sample2.databinding.ActivityP4C03MainBinding
import com.minee.kotlin_sample2.part4chapter03.P4C03MapActivity.Companion.EXTRA_SEARCH_RESULT
import com.minee.kotlin_sample2.part4chapter03.model.LocationLatLngEntity
import com.minee.kotlin_sample2.part4chapter03.model.SearchResultEntity
import com.minee.kotlin_sample2.part4chapter03.response.search.Poi
import com.minee.kotlin_sample2.part4chapter03.util.RetrofitUtil
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class P4C03MainActivity : AppCompatActivity(), CoroutineScope {

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private lateinit var binding: ActivityP4C03MainBinding
    private var searchResultAdapter: SearchResultAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityP4C03MainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        job = Job()

        initViews()
        bindViews()
        initRecyclerView()
    }

    private fun initViews() = with(binding) {
        emptyResultTextView.isVisible = false
    }

    private fun bindViews() = with(binding) {
        searchButton.setOnClickListener {
            searchKeyword(searchEditText.text.toString())
        }
    }

    private fun initRecyclerView() {
        searchResultAdapter = SearchResultAdapter(onClickListener = { searchResultEntity ->
            //Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(this, P4C03MapActivity::class.java).apply {
                    putExtra(EXTRA_SEARCH_RESULT, searchResultEntity)
                }
            )
        })
        binding.searchResultRecyclerView.apply {
            adapter = searchResultAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun searchKeyword(keyword: String) {
        launch(coroutineContext) {
            try {
                withContext(Dispatchers.IO) {
                    val response = RetrofitUtil.apiService.getSearchLocation(
                        keyword = keyword
                    )
                    if (response.isSuccessful) {
                        val body = response.body()
                        withContext(Dispatchers.Main) {
                            Log.d("minee", body.toString())
                            body?.let { searchResponse ->
                                setData(searchResponse.searchPoiInfo.pois.poi)
                            }
                        }
                    }
                }
            } catch (e: Exception) {

            }
        }
    }

    private fun setData(pois: List<Poi>) {
        val searchResults = pois.mapIndexed { index, poi ->
            SearchResultEntity(
                id = index.toLong(),
                name = poi.name ?: "명칭 없음",
                fullAddress = poi.newAddressList.newAddress[0].fullAddressRoad ?: "주소 없음",
                locationLatLng = LocationLatLngEntity(
                    poi.noorLat.toFloat(),
                    poi.noorLon.toFloat()
                )
            )
        }
        searchResultAdapter?.submitList(searchResults)
    }

}