package com.minee.kotlin_sample2.part4chapter01.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.minee.kotlin_sample2.databinding.ItemVideoBinding
import com.minee.kotlin_sample2.part4chapter01.model.VideoModel

class VideoAdapter(val callback: (String, String) -> Unit) :
    ListAdapter<VideoModel, VideoAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(videoModel: VideoModel) {
            binding.titleTextView.text = videoModel.title
            binding.subtitleTextView.text = videoModel.subtitle

            if (videoModel.thumb.isNullOrEmpty().not()) {
                Glide.with(binding.thumbnailImageView)
                    .load(videoModel.thumb)
                    .into(binding.thumbnailImageView)
            }

            binding.root.setOnClickListener {
                callback(videoModel.sources, videoModel.title)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemVideoBinding.inflate(
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
        val diffUtil = object : DiffUtil.ItemCallback<VideoModel>() {
            override fun areItemsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: VideoModel, newItem: VideoModel): Boolean {
                return oldItem == newItem
            }

        }
    }
}