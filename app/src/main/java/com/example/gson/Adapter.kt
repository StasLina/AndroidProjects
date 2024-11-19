package com.example.gson

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import java.io.ByteArrayOutputStream
import kotlin.reflect.typeOf

class Adapter() : RecyclerView.Adapter<Adapter.MyViewHolder>() {
    //var photoList :List<Photo>
    private var photoList = listOf<Photo>()

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view),View.OnClickListener {
        val imageView: ImageView = view.findViewById(R.id.imageView)

        // Переопределяем событие нажатия
        public fun onImageClick(image: ImageView){
            image.setImageDrawable(image.drawable)
            val bitmap = (image.drawable as BitmapDrawable).bitmap
            Timber.v("Click on Image")
            Timber.v("Pucture Height ${bitmap.height} Width ${bitmap.width}")

            // Сохраняем картинку
            //val bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val b = baos.toByteArray()

            val intent = Intent(view.context, PicViewer::class.java)
            intent.putExtra("picture", b)
            view.context.startActivity(intent)
        }

        override fun onClick(p0: View?) {
            if (p0 is ImageView) {
                val image = p0 as ImageView;
                onImageClick(image)
            }
        }
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
            .into(holder.imageView) // Устанавливаем изображение в imageView

        // Добавляем событие OnClick
        holder.imageView.setOnClickListener(holder)

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