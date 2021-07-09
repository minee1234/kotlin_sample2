package com.minee.kotlin_sample2.part4chapter02

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.minee.kotlin_sample2.databinding.ItemMusicBinding

class PlaylistAdapter(private val callback: (MusicModel) -> Unit) :
    ListAdapter<MusicModel, PlaylistAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(musicModel: MusicModel) {
            binding.itemTrackTextView.text = musicModel.track
            binding.itemArtistTextView.text = musicModel.artist

            if (musicModel.coverUrl.isNullOrEmpty().not()) {
                Glide.with(binding.itemCoverImageView)
                    .load(musicModel.coverUrl)
                    .into(binding.itemCoverImageView)
            }

            if (musicModel.isPlaying) {
                itemView.setBackgroundColor(Color.GRAY)
            } else {
                itemView.setBackgroundColor(Color.TRANSPARENT)
            }

            itemView.setOnClickListener {
                callback(musicModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMusicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<MusicModel>() {
            override fun areItemsTheSame(oldItem: MusicModel, newItem: MusicModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MusicModel, newItem: MusicModel): Boolean {
                return oldItem == newItem
            }

        }
    }
}