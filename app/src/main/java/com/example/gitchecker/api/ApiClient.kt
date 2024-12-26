package com.example.gitchecker.api

import android.util.Log
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    private var retrofit: Retrofit? = null

    fun getClient(
        protocol: String,
        address: String,
        path: String,
        username: String?,
        password: String?
    ): Retrofit {
        val baseUrl = "$protocol://$address/$path"

        Log.d("APIClient", "baseUrl= ${baseUrl}" )

        val client: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original: Request = chain.request()
                val request = original.newBuilder().apply {
                    if (!username.isNullOrEmpty() && !password.isNullOrEmpty()) {
                        header("Authorization", Credentials.basic(username, password))
                    }
                }.method(original.method, original.body)
                    .build()
                chain.proceed(request)
            }
            .build()

        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}

