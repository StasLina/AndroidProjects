package com.example.gson

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import okhttp3.*
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity(), ActionOnResultSuccess {

    private lateinit var recyclerView: RecyclerView
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree()) // Инициализация Timber

        Timber.v("Начало инициализации")
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Timber.v("resultLauncher Press F")
                if (result.resultCode == Activity.RESULT_OK) {
                    // Обрабатываем результат
                    val data = result.data
                    val imageUrl = data?.getStringExtra("imageUrl")

                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Картинка добавлена в избранное",
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("Открыть") {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl))
                            startActivity(browserIntent)
                        }
                        .show()
                }
            }

        Timber.v("Search RecycleView")
        recyclerView = findViewById(R.id.rView)

        // Иницилизируем адаптер
        val adapter = Adapter(onImageClick = ::openPicViewer)
        recyclerView.adapter = adapter
        Timber.v("Set adapter")
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val apiInstance = API()
        apiInstance.fetchPhotos(this)
    }

    override fun ActionOnResultSuccess(eventData: String) {
        val gson = Gson()
        val wrapper = gson.fromJson(eventData, Wrapper::class.java)

        runOnUiThread {
            val adapter = (recyclerView.adapter as Adapter)
            adapter.setPhotos(wrapper.photos.photo)
        }
    }

    //@Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    /*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Timber.v("OnActivity Result")
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            val imageUrl = data.getStringExtra("imageUrl")

                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Картинка добавлена в избранное",
                    Snackbar.LENGTH_LONG
                )
                    .setAction("Открыть") {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl))
                        startActivity(browserIntent)
                    }
                    .show()
        }
    }*/


    // Запуск активности с ожиданием результата
    public fun openPicViewer(image: ImageView, photoData: Photo) {
        Timber.v("openPicViewer")



        // Запуск активности с ожиданием результата
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        Timber.v("Click on Image")
        Timber.v("Pucture Height ${bitmap.height} Width ${bitmap.width}")

        // Сохраняем картинку
        //val bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()

        val intent = Intent(this, PicViewer::class.java)
        intent.putExtra("picture", b)
        intent.putExtra("data", photoData)
        //startActivity(intent)
        resultLauncher.launch(intent)
        //view.context.startActivity(intent)
    }
    //old version
    /*
    public fun openPicViewer(image: ImageView, photoData: Photo){
        val bitmap = (image.drawable as BitmapDrawable).bitmap
        Timber.v("Click on Image")
        Timber.v("Pucture Height ${bitmap.height} Width ${bitmap.width}")

        // Сохраняем картинку
        //val bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()

        val intent = Intent(this, PicViewer::class.java)
        intent.putExtra("picture", b)
        intent.putExtra("data", photoData)
        startActivity(intent)
        //view.context.startActivity(intent)
    }
     */

}
