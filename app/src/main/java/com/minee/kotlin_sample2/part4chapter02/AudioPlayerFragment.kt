package com.minee.kotlin_sample2.part4chapter02

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.minee.kotlin_sample2.R
import com.minee.kotlin_sample2.databinding.FragmentAudioPlayerBinding
import com.minee.kotlin_sample2.part4chapter02.service.MusicDto
import com.minee.kotlin_sample2.part4chapter02.service.MusicService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AudioPlayerFragment : Fragment(R.layout.fragment_audio_player) {

    private var playerModel: PlayerModel = PlayerModel()
    private var binding: FragmentAudioPlayerBinding? = null
    private var player: SimpleExoPlayer? = null
    private lateinit var playlistAdapter: PlaylistAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentAudioPlayerBinding = FragmentAudioPlayerBinding.bind(view)
        binding = fragmentAudioPlayerBinding

        initPlayerView(fragmentAudioPlayerBinding)
        initPlaylistButton(fragmentAudioPlayerBinding)
        initPlayControlButtons(fragmentAudioPlayerBinding)
        initRecyclerView(fragmentAudioPlayerBinding)

        getAudioListFromServer()
    }

    private fun initPlayControlButtons(fragmentAudioPlayerBinding: FragmentAudioPlayerBinding) {
        fragmentAudioPlayerBinding.playControlImageView.setOnClickListener {
            val player = this.player ?: return@setOnClickListener
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }
        fragmentAudioPlayerBinding.skipNextImageView.setOnClickListener {
            val player = this.player ?: return@setOnClickListener

        }
        fragmentAudioPlayerBinding.skipPrevImageView.setOnClickListener {
            val player = this.player ?: return@setOnClickListener
        }
    }

    private fun initPlayerView(fragmentAudioPlayerBinding: FragmentAudioPlayerBinding) {
        context?.let {
            player = SimpleExoPlayer.Builder(it).build()
        }
        fragmentAudioPlayerBinding.playerView.player = player
        binding?.let { binding ->
            player?.addListener(object: Player.Listener{
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    if (isPlaying) {
                        binding.playControlImageView.setImageResource(R.drawable.ic_pause_48)
                    } else {
                        binding.playControlImageView.setImageResource(R.drawable.ic_play_arrow_48)
                    }
                }
            })
        }
    }

    private fun initRecyclerView(fragmentAudioPlayerBinding: FragmentAudioPlayerBinding) {
        playlistAdapter = PlaylistAdapter {
            // todo 음악 재생
        }

        fragmentAudioPlayerBinding.playlistRecyclerView.apply {
            adapter = playlistAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initPlaylistButton(fragmentAudioPlayerBinding: FragmentAudioPlayerBinding) {
        fragmentAudioPlayerBinding.playlistImageView.setOnClickListener {
            // todo 서버에서 데이터를 받아오지 않은 상태일 때

            fragmentAudioPlayerBinding.playerViewGroup.isVisible = playerModel.isWatchingPlaylistView
            fragmentAudioPlayerBinding.playlistViewGroup.isVisible = playerModel.isWatchingPlaylistView.not()

            playerModel.isWatchingPlaylistView = !playerModel.isWatchingPlaylistView
        }
    }


    private fun getAudioListFromServer() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(MusicService::class.java).also { it ->
            it.listMusics()
                .enqueue(object : Callback<MusicDto> {
                    override fun onResponse(call: Call<MusicDto>, response: Response<MusicDto>) {
                        Log.d("minee", "${response.body()}")
                        
                        response.body()?.let { musicDto ->
                            val musicModelList = musicDto.musics.mapIndexed { index, musicEntity ->
                                musicEntity.mapper(index.toLong())
                            }

                            setMusicList(musicModelList)
                            playlistAdapter.submitList(musicModelList)
                        }
                    }

                    override fun onFailure(call: Call<MusicDto>, t: Throwable) {

                    }

                })
        }
    }

    private fun setMusicList(musicModelList: List<MusicModel>) {
        context?.let {
            player?.setMediaItems(musicModelList.map { musicModel ->
                MediaItem.Builder()
                    .setMediaId(musicModel.id.toString())
                    .setUri(musicModel.streamUrl)
                    .build()
            })
            player?.prepare()
            player?.play()
        }
    }

    companion object {
        fun newInstance(): AudioPlayerFragment {
            return AudioPlayerFragment()
        }
    }
}