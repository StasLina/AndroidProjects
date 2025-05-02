package com.example.navigationbetweenscreens.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.navigationbetweenscreens.databinding.ItemImageBinding

class PictureAdapter(
    private val onItemClick: (String) -> Unit,
    private val onItemLongClick: (String) -> Unit
) : ListAdapter<String, PictureAdapter.PictureViewHolder>(PictureDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val binding = ItemImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PictureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val imageUri = getItem(position)
        holder.bind(imageUri)
    }

    inner class PictureViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUri: String) {
            Glide.with(binding.root)
                .load(imageUri)
                .centerCrop()
                .into(binding.imageView)

            binding.root.setOnClickListener {
                onItemClick(imageUri)
            }

            binding.root.setOnLongClickListener {
                onItemLongClick(imageUri)
                true
            }
        }
    }

    private class PictureDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}