package com.minee.kotlin_sample2.part4chapter02

data class PlayerModel(
    private val playMusicList: List<MusicModel> = emptyList(),
    var currentPosition: Int = -1,
    var isWatchingPlaylistView: Boolean = true
) {
    fun getAdapterModels(): List<MusicModel> {
        return playMusicList.mapIndexed { index, musicModel ->
            val newItem = musicModel.copy(
                isPlaying = index == currentPosition
            )
            newItem
        }
    }

    fun updateCurrentPosition(musicModel: MusicModel) {
        currentPosition = playMusicList.indexOf(musicModel)
    }

    fun nextMusic(): MusicModel? {
        if (playMusicList.isEmpty()) return null

        currentPosition++
        if (currentPosition >= playMusicList.size) {
            currentPosition = 0
        }

        return playMusicList[currentPosition]
    }

    fun prevMusic(): MusicModel? {
        if (playMusicList.isEmpty()) return null

        currentPosition = if ((currentPosition-1) < 0) {
            playMusicList.lastIndex
        } else {
            currentPosition - 1
        }

        return playMusicList[currentPosition]
    }

    fun currentMusicModel(): MusicModel? {
        if (playMusicList.isEmpty()) return null

        return playMusicList[currentPosition]
    }
}
