package com.minee.kotlin_sample2.part4chapter01

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.minee.kotlin_sample2.R
import com.minee.kotlin_sample2.part4chapter01.adapter.VideoAdapter
import com.minee.kotlin_sample2.part4chapter01.dto.VideoDto
import com.minee.kotlin_sample2.part4chapter01.service.VideoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class P4C01MainActivity : AppCompatActivity() {

    private lateinit var videoAdapter: VideoAdapter

    private val mainRecyclerView: RecyclerView by lazy {
        findViewById(R.id.mainRecyclerView)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_p4_c01_main)

        videoAdapter = VideoAdapter(callback = { url, title ->
            // Tag를 설정하지 않은 경우
            /*
            supportFragmentManager.fragments.find {
                it is PlayerFragment
            }?.let {
                (it as PlayerFragment).play(url, title)
            }*/

            // Tag를 설정한 경우
            supportFragmentManager.findFragmentByTag(TAG_PLAYER_FRAGMENT)?.let {
                (it as PlayerFragment).play(url, title)
            }
        })
        mainRecyclerView.adapter = videoAdapter
        mainRecyclerView.layoutManager = LinearLayoutManager(this)

        supportFragmentManager.beginTransaction()
            //.replace(R.id.fragmentContainer, PlayerFragment())
            .replace(R.id.fragmentContainer, PlayerFragment(), TAG_PLAYER_FRAGMENT)
            .commit()

        getVideoList()
    }

    private fun getVideoList() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(VideoService::class.java).also { videoService ->
            videoService.listVideos()
                .enqueue(object : Callback<VideoDto> {
                    override fun onResponse(call: Call<VideoDto>, response: Response<VideoDto>) {
                        if (response.isSuccessful.not()) {
                            Log.d("minee", "response fail!!")
                            return
                        }

                        response.body()?.let { videoDto ->
                            Log.d("minee", videoDto.toString())
                            videoAdapter.submitList(videoDto.videos)
                        }
                    }

                    override fun onFailure(call: Call<VideoDto>, t: Throwable) {

                    }

                })
        }
    }

    companion object {
        const val TAG_PLAYER_FRAGMENT = "PlayerFragment"
    }
}