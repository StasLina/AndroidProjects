package com.example.gson

// /*
import com.example.gson.Wrapper
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.io.IOException

interface ActionOnResultSuccess {
    fun ActionOnResultSuccess(eventData: String);
}

class API {

    fun fetchPhotos(action : ActionOnResultSuccess? = null) {
        val client = OkHttpClient()
        val url =
            "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1"

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Timber.e(e, "Request Failed")
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body?.string()

                if (response.isSuccessful && !json.isNullOrEmpty()) {
                    parsePhotos(json)
                }

                if(action != null && json != null) {
                    action.ActionOnResultSuccess(json)
                }
            }
        })
    }

    fun parsePhotos(json: String) {
        val gson = Gson()
        val wrapper = gson.fromJson(json, Wrapper::class.java)

        wrapper.photos.photo.forEachIndexed { index, photo ->
            if ((index + 1) % 5 == 0) {
                Timber.d("Every 5th photo: $photo")
            }
        }
    }
}
// */
