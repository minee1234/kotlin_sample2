package com.minee.kotlin_sample2.part4chapter02

data class PlayerModel(
    val playMusicList: List<MusicModel> = emptyList(),
    val currentPosition: Int = -1,
    var isWatchingPlaylistView: Boolean = true
) {

}
