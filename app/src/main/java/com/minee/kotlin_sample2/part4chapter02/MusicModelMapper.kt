package com.minee.kotlin_sample2.part4chapter02

import com.minee.kotlin_sample2.part4chapter02.service.MusicDto
import com.minee.kotlin_sample2.part4chapter02.service.MusicEntity

fun MusicEntity.mapper(id: Long): MusicModel =
    MusicModel(
        id = id,
        track = track,
        streamUrl = streamUrl,
        artist = artist,
        coverUrl = coverUrl
    )

fun MusicDto.mapper() : PlayerModel =
    PlayerModel(
        playMusicList = musics.mapIndexed { index, musicEntity ->
            musicEntity.mapper(index.toLong())
        }
    )