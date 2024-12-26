package com.example.gitchecker.models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.gitchecker.MyApplication
import com.example.gitchecker.api.UserResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AppData (application: Application) : AndroidViewModel(application) {
    var data: AppDataMain = (application as MyApplication).GetData()
    var dataHard : HardData = HardData.getInstance(application)
}

class AppDataMain  {
    var userData: UserResponse? = null

//    private  var protocol: String? = null
//    fun setProtocol(protocol: String) {
//        this.protocol = protocol
//    }
//
//    fun getProtocol(): String? {
//        return protocol
//    }
//
//    private var login: String? = null
//
//    fun setLogin(login: String) {
//        this.login = login
//    }
//
//    fun getLogin(): String? {
//        return login
//    }
//
//    private var password: String? = null
//
//    fun setPassword(password: String) {
//        this.password = password
//    }
//
//    // Метод для получения пароля
//    fun getPassword(): String? {
//        return password
//    }
}

