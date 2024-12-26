package com.example.gitchecker

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.gitchecker.api.ApiClient
import com.example.gitchecker.api.ApiService
import com.example.gitchecker.api.fetchUserData

import com.example.gitchecker.models.AppData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class LoginViewModel() : ViewModel() {
    private val _login = MutableLiveData<String>()
    val login: LiveData<String> get() = _login

    fun setLogin(newName: String) {
        _login.value = newName
    }

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> get() = _password

    fun setPassword(newPassword: String) {
        _password.value = newPassword
    }
}

class LoginActivity : AppCompatActivity() {

    private val appData: AppData by viewModels()

    private val loginViewMode: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Связываем макет с Activity

        setContentView(R.layout.activity_login)

        loginViewMode.login.observe(this, Observer { newName ->
            // Обновляем UI (например, изменяем текст в TextView)
            findViewById<EditText>(R.id.login).setText(newName)
        })

        loginViewMode.password.observe(this, Observer { newPassword ->
            // Обновляем UI (например, изменяем текст в TextView)
            findViewById<EditText>(R.id.password).setText(newPassword)
        })

        // Для каждого collect вызываем в отдельной корутине
        lifecycleScope.launch {
            appData.dataHard.login.collect { login ->
                login?.takeIf { it.isNotEmpty() }?.let {
                    Log.d("LoginActivity", "finded login: $it")
                    loginViewMode.setLogin(it)
                }
            }
        }

        lifecycleScope.launch {
            appData.dataHard.password.collect { password ->
                password?.takeIf { it.isNotEmpty() }?.let {
                    Log.d("LoginActivity", "finded password: $it")
                    loginViewMode.setPassword(it)
                }
            }
        }

        val btnLogin: Button = findViewById(R.id.LoginClick)
        val btnSettings: ImageView = findViewById(R.id.ConnectionSettings)
        val bExit: ImageView = findViewById(R.id.Close)

//      Вызывает бексонечный цикл
//        val loginView: TextView = findViewById(R.id.login)
//        loginView.addTextChangedListener {
//            loginViewMode.setLogin(it.toString())
//        }
//
//        val passwordView: TextView = findViewById(R.id.password)
//        passwordView.addTextChangedListener {
//            loginViewMode.setPassword(it.toString())
//        }

        btnLogin.setOnClickListener {
            // Запускаем одну корутину для выполнения всех шагов последовательно
            lifecycleScope.launch {
                try {
                    // Сохраняем данные авторизации
                    val loginView: TextView = findViewById(R.id.login)
                    val passwordView: TextView = findViewById(R.id.password)

                    loginViewMode.setLogin(loginView.text.toString())
                    loginViewMode.setPassword(passwordView.text.toString())

                    // Логируем попытку сохранить логин
                    loginViewMode.login.value?.takeIf { it.isNotEmpty() }?.let {
                        Log.d("LoginActivity", "Saving login: $it")
                        appData.dataHard.saveLogin(it)
                    } ?: Log.d("LoginActivity", "Login is empty or null, not saving.")

                    // Логируем попытку сохранить пароль
                    loginViewMode.password.value?.takeIf { it.isNotEmpty() }?.let {
                        Log.d("LoginActivity", "Saving password: $it")
                        appData.dataHard.savePassword(it)
                    } ?: Log.d("LoginActivity", "Password is empty or null, not saving.")

                    // Проверяем установку пароля
                    Log.d("LoginActivity", "CheckUpdate: Password_")
                    val passwordValue = appData.dataHard.password.first() // { passwordValue ->
                        passwordValue?.let {
                            Log.d("LoginActivity", "Getting password: $it")
                        } ?: Log.d("LoginActivity", "Password not set or empty")
//                    }

                    // Проверяем установку логина
                    Log.d("LoginActivity", "CheckUpdate: Login")
                    val loginValue = appData.dataHard.login.first() // { loginValue ->
                        loginValue?.let {
                            Log.d("LoginActivity", "Getting login: $it")
                        } ?: Log.d("LoginActivity", "Login not set or empty")
//                    }

                    // Выполняем авторизацию через API
                    val apiClient = ApiClient()
                    Log.d("LoginActivity", "Calling getConnectionParams")
                    val conParams = getConnectionParams()

                    Log.d("LoginActivity", "Calling getClient")
                    val retrofit = apiClient.getClient(
                        protocol = conParams[0], // "http",
                        address = conParams[1], // "192.192.56.111:3000",
                        path = "api/v1/",
                        username = conParams[2], // "Stanislav",
                        password = conParams[3] // "i2Ekbi9p.JYegiz"
                    )

//
//                    val retrofit = apiClient.getClient(
//                        protocol = "http", // "http",
//                        address ="192.192.56.111:3000", // "192.192.56.111:3000",
//                        path = "api/v1/",
//                        username = "Stanislav", // "Stanislav",
//                        password = "i2Ekbi9p.JYegiz"// "i2Ekbi9p.JYegiz"
//                    )

                    Log.d("LoginActivity", "Create api service")
                    val apiService = retrofit.create(ApiService::class.java)

                    Log.d("LoginActivity", "Calling fetchUserData")
                    fetchUserData(apiService,
                        onSuccess = { user ->
                            //withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@LoginActivity, // Правильный контекст
                                "Успешная авторизация!",
                                Toast.LENGTH_SHORT
                            ).show()
                            //}
                            Log.d("LoginActivity", "Autorize success ${user.loginName}")
                        },
                        onError = { errorMessage ->
                            //withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@LoginActivity, // Правильный контекст
                                "Логин или пароль не верны!",
                                Toast.LENGTH_SHORT
                            ).show()
                            Toast.makeText(
                                this@LoginActivity, // Правильный контекст
                                errorMessage,
                                Toast.LENGTH_LONG
                            ).show()
                            //}
                        }
                    )
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@LoginActivity, // Правильный контекст
                            ex.message ?: "Произошла ошибка",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    Log.e("LoginActivity", "Error occurred: ${ex.message}", ex)
                }
            }

            // Обработчики кликов для других кнопок
            btnSettings.setOnClickListener {
                val intent = Intent(this@LoginActivity, SettingsActivity::class.java)
                startActivity(intent)
            }

            bExit.setOnClickListener {
                Toast.makeText(this@LoginActivity, "Выходим!", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    // Так точно не надо делать получается каша
    suspend fun getConnectionParams(
    ): Array<String> {

        var protocol = "";
        var address = "";
        var username = "";
        var password = "";

//        lifecycleScope.launch {
        Log.d("LoginActivity", "CheckUpdate")
        val passwordValue = appData.dataHard.password.first()
        passwordValue?.let {
            Log.d("LoginActivity", "Getting password: $it")
            password = it;
        } ?: Log.d("LoginActivity", "password not set or empty")

//        }
//        lifecycleScope.launch {
        Log.d("LoginActivity", "CheckUpdate")
        val loginValue = appData.dataHard.login.first()
        loginValue?.let {
            Log.d("LoginActivity", "Getting login: $it")
            username = it;
        } ?: Log.d("LoginActivity", "Login not set or empty")

//        }

//        lifecycleScope.launch {
        Log.d("LoginActivity", "CheckUpdate")
        val addressValue = appData.dataHard.address.first() // { passwordValue ->
        addressValue?.let {
                Log.d("LoginActivity", "Getting address: $it")
                address = it;
            } ?: Log.d("LoginActivity", "password not set or empty")

//        }
//        }
//        lifecycleScope.launch {
//
        Log.d("LoginActivity", "CheckUpdate")
        var  protocolValue = appData.dataHard.protocol.first()// { loginValue ->
        protocolValue?.let {
                Log.d("LoginActivity", "Getting protocol: $it")
                protocol = it;
        } ?: Log.d("LoginActivity", "Login not set or empty")
//        }
//        }

        Log.d("LoginActivity", "Login data ${protocol} ${address} ${username}")
        return arrayOf(protocol, address, username, password)
    }
}