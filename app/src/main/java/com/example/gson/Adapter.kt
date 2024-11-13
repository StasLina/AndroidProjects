package com.example.gson

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import java.io.ByteArrayOutputStream

class Adapter(
    private val onImageClick: (image: ImageView, photoData: Photo) -> Unit
) : RecyclerView.Adapter<Adapter.MyViewHolder>() {
    private var photoList = listOf<Photo>()

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {//View.OnClickListener
        val imageView: ImageView = view.findViewById(R.id.imageView)

        // Переопределяем событие нажатия
        // Depracted
        public fun onImageClick(image: ImageView, photoData: Photo){
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
            intent.putExtra("data", photoData)
            view.context.startActivity(intent)
        }

        /* Depracted
        override fun onClick(p0: View?) {
            if (p0 is ImageView) {
                val image = p0 as ImageView;
                onImageClick(image)
            }
        }
        */
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.rview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Timber.v("onBindViewHolder")
        val photo = photoList[position]
        val imageUrl = photo.getURI()


        Timber.v("Loading image from URL: $imageUrl")

        // Загрузка изображения с помощью Glide
        GlideApp.with(holder.view.context)
            .load(imageUrl)
            .placeholder(R.drawable.loading_svgrepo_com)
            .error(R.drawable.error_player_multimedia_svgrepo_com)
            .into(holder.imageView) // Устанавливаем изображение в imageView

        // Добавляем событие OnClick
        //holder.imageView.setOnClickListener(holder)
        holder.imageView.setOnClickListener{
            //holder.onImageClick(holder.imageView,photo)
            onImageClick(holder.imageView, photo)
        }
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