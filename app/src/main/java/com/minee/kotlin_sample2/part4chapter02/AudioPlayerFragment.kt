package com.minee.kotlin_sample2.part4chapter02

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SeekBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
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
import java.util.concurrent.TimeUnit

class AudioPlayerFragment : Fragment(R.layout.fragment_audio_player) {

    private val updateSeekRunnable = Runnable {
        updateSeek()
    }
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
        initSeekBar(fragmentAudioPlayerBinding)
        initRecyclerView(fragmentAudioPlayerBinding)

        getAudioListFromServer()
    }

    private fun initSeekBar(fragmentAudioPlayerBinding: FragmentAudioPlayerBinding) {
        fragmentAudioPlayerBinding.playerSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    player?.pause()
                    binding?.let {
                        it.playTimeTextView.text = String.format(
                            "%02d:%02d",
                            TimeUnit.SECONDS.toMinutes(progress.toLong()),
                            progress % 60
                        )
                    }
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                player?.seekTo((seekBar.progress * 1000).toLong())
                player?.play()
            }

        })
        fragmentAudioPlayerBinding.playlistSeekBar.setOnTouchListener { v, event ->
            true
        }
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
            val nextMusic = playerModel.nextMusic() ?: return@setOnClickListener
            playMusic(nextMusic)
        }
        fragmentAudioPlayerBinding.skipPrevImageView.setOnClickListener {
            val prevMusic = playerModel.prevMusic() ?: return@setOnClickListener
            playMusic(prevMusic)
        }
    }

    private fun initPlayerView(fragmentAudioPlayerBinding: FragmentAudioPlayerBinding) {
        context?.let {
            player = SimpleExoPlayer.Builder(it).build()
        }
        fragmentAudioPlayerBinding.playerView.player = player
        binding?.let { binding ->
            player?.addListener(object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)
                    if (isPlaying) {
                        binding.playControlImageView.setImageResource(R.drawable.ic_pause_48)
                    } else {
                        binding.playControlImageView.setImageResource(R.drawable.ic_play_arrow_48)
                    }
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    val newIndex = mediaItem?.mediaId ?: return
                    playerModel.currentPosition = newIndex.toInt()
                    updatePlayerView(playerModel.currentMusicModel())
                    playlistAdapter.submitList(playerModel.getAdapterModels())
                }

                override fun onPlaybackStateChanged(state: Int) {
                    super.onPlaybackStateChanged(state)
                    updateSeek()
                }
            })
        }
    }

    private fun updateSeek() {
        val player = this.player ?: return

        val dulation = if (player.duration >= 0) player.duration else 0
        val position = player.currentPosition
        updateSeekUi(dulation, position)

        val state = player.playbackState
        view?.removeCallbacks(updateSeekRunnable)
        if (state != Player.STATE_IDLE && state != Player.STATE_ENDED) {
            view?.postDelayed(updateSeekRunnable, 1000)
        }

    }

    private fun updateSeekUi(dulation: Long, position: Long) {
        binding?.let { binding ->
            binding.playlistSeekBar.max = (dulation / 1000).toInt()
            binding.playlistSeekBar.progress = (position / 1000).toInt()

            binding.playerSeekBar.max = (dulation / 1000).toInt()
            binding.playerSeekBar.progress = (position / 1000).toInt()

            binding.playTimeTextView.text = String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(position),
                (position / 1000) % 60
            )
            binding.totalTimeTextView.text = String.format(
                "%02d:%02d",
                TimeUnit.MINUTES.convert(dulation, TimeUnit.MILLISECONDS),
                (dulation / 1000) % 60
            )
        }
    }

    private fun updatePlayerView(currentMusicModel: MusicModel?) {
        currentMusicModel ?: return

        binding?.let { binding ->
            binding.trackTextView.text = currentMusicModel.track
            binding.artistTextView.text = currentMusicModel.artist
            Glide.with(binding.coverImageView.context)
                .load(currentMusicModel.coverUrl)
                .into(binding.coverImageView)
        }
    }

    private fun initRecyclerView(fragmentAudioPlayerBinding: FragmentAudioPlayerBinding) {
        playlistAdapter = PlaylistAdapter {
            playMusic(it)
        }

        fragmentAudioPlayerBinding.playlistRecyclerView.apply {
            adapter = playlistAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initPlaylistButton(fragmentAudioPlayerBinding: FragmentAudioPlayerBinding) {
        fragmentAudioPlayerBinding.playlistImageView.setOnClickListener {
            if (playerModel.currentPosition == -1) {
                return@setOnClickListener
            }

            fragmentAudioPlayerBinding.playerViewGroup.isVisible =
                playerModel.isWatchingPlaylistView
            fragmentAudioPlayerBinding.playlistViewGroup.isVisible =
                playerModel.isWatchingPlaylistView.not()

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
                            playerModel = musicDto.mapper()

                            setMusicList(playerModel.getAdapterModels())
                            playlistAdapter.submitList(playerModel.getAdapterModels())
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
        }
    }

    private fun playMusic(musicModel: MusicModel) {
        playerModel.updateCurrentPosition(musicModel)
        player?.seekTo(playerModel.currentPosition, 0)
        player?.play()
    }

    override fun onStop() {
        super.onStop()
        player?.pause()
        view?.removeCallbacks(updateSeekRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        player?.release()
        view?.removeCallbacks(updateSeekRunnable)
    }

    companion object {
        fun newInstance(): AudioPlayerFragment {
            return AudioPlayerFragment()
        }
    }
}