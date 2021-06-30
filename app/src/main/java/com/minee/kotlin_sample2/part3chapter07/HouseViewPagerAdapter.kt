package com.minee.kotlin_sample2.part3chapter07

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.minee.kotlin_sample2.databinding.ItemHouseDetailForViewpagerBinding

class HouseViewPagerAdapter(val itemClicked: (HouseModel) -> Unit) :
    ListAdapter<HouseModel, HouseViewPagerAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(val binding: ItemHouseDetailForViewpagerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(houseModel: HouseModel) {
            if (houseModel.imgUrl?.isNotEmpty()) {
                Glide.with(binding.thumbnailImageView)
                    .load(houseModel.imgUrl)
                    .into(binding.thumbnailImageView)
            }

            binding.titleTextView.text = houseModel.title
            binding.priceTextView.text = houseModel.price

            binding.root.setOnClickListener {
                itemClicked(houseModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHouseDetailForViewpagerBinding.inflate(
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
        val diffUtil = object : DiffUtil.ItemCallback<HouseModel>() {
            override fun areItemsTheSame(oldItem: HouseModel, newItem: HouseModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: HouseModel, newItem: HouseModel): Boolean {
                return oldItem == newItem
            }

        }
    }
}