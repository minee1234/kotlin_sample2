package com.minee.kotlin_sample2.part4chapter03

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.minee.kotlin_sample2.databinding.ItemMapSearchResultBinding
import com.minee.kotlin_sample2.part4chapter03.model.SearchResultEntity

class SearchResultAdapter(val onClickListener: (SearchResultEntity) -> Unit) :
    ListAdapter<SearchResultEntity, SearchResultAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding: ItemMapSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchResultEntity) {
            with(binding) {
                titleTextView.text = item.name
                subtitleTextView.text = item.fullAddress
            }

            binding.root.setOnClickListener {
                onClickListener(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMapSearchResultBinding.inflate(
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
        val diffUtil = object : DiffUtil.ItemCallback<SearchResultEntity>() {
            override fun areItemsTheSame(
                oldItem: SearchResultEntity,
                newItem: SearchResultEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: SearchResultEntity,
                newItem: SearchResultEntity
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}