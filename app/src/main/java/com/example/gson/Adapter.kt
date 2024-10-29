package com.example.gson

import android.annotation.SuppressLint
import com.bumptech.glide.Glide
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson

import timber.log.Timber

class Adapter() : RecyclerView.Adapter<Adapter.MyViewHolder>() {
    //var photoList :List<Photo>
    private var photoList = listOf<Photo>()

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.rview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Timber.v("onBindViewHolder")
        val photo = photoList[position]
        val imageUrl =
            "https://farm${photo.farm}.staticflickr.com/${photo.server}/${photo.id}_${photo.secret}_z.jpg"

        Timber.v("Loading image from URL: $imageUrl")

        // Загрузка изображения с помощью Glide
        GlideApp.with(holder.view.context)
            .load(imageUrl)
            .placeholder(R.drawable.loading_svgrepo_com)
            .error(R.drawable.error_player_multimedia_svgrepo_com)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPhotos(newPhotos: List<Photo>) {
        photoList = newPhotos
        this.notifyDataSetChanged()
        Timber.v("this.notifyDataSetChanged. New count ${this.getItemCount()}")
    }
}
// */