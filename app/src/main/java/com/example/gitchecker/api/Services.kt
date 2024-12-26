package com.example.gitchecker.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("user")
    fun getUser(): Call<UserResponse>

}

fun fetchUserData(apiService: ApiService, onSuccess: (UserResponse) -> Unit, onError: (String) -> Unit) {
    apiService.getUser().enqueue(object : Callback<UserResponse> {
        override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
            when {
                response.isSuccessful -> {
                    val user = response.body()
                    if (user != null) {
                        onSuccess(user) // Передаем результат успешного запроса
                    } else {
                        onError("Ответ успешный, но тело пустое.")
                    }
                }
                response.code() == 401 -> {
                    onError("Ошибка авторизации (401): Проверьте логин и пароль.")
                }
                else -> {
                    onError("Неожиданный ответ: ${response.code()}")
                }
            }
        }

        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            if (t.message?.contains("timeout") == true) {
                onError("Ошибка: Превышено время ожидания.")
            } else {
                onError("Ошибка: ${t.message}")
            }
        }
    })
}