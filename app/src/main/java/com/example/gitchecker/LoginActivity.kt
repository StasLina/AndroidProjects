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

import com.example.gitchecker.models.AppData
import kotlinx.coroutines.launch
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

        lifecycleScope.launch{
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

        btnLogin.setOnClickListener{
            lifecycleScope.launch {
                try {

                    val loginView: TextView = findViewById(R.id.login)
                    val passwordView: TextView = findViewById(R.id.password)
//
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

                } catch (e: Exception) {  // Используйте Exception для захвата любых ошибок
                    Log.e("LoginActivity", "Error occurred: ${e.message}", e)
                }
            }

            // Проверяем установку
            lifecycleScope.launch {
                Log.d("LoginActivity", "CheckUpdate")
                appData.dataHard.password.collect { passwordValue ->
                    passwordValue?.let {
                        Log.d("LoginActivity", "Getting password: $it")
                    } ?: Log.d("LoginActivity", "password not set or empty")
                }
            }

            lifecycleScope.launch {
                Log.d("LoginActivity", "CheckUpdate")
                appData.dataHard.login.collect { loginValue ->
                    loginValue?.let {
                        Log.d("LoginActivity", "Getting login: $it")
                    } ?: Log.d("LoginActivity", "Login not set or empty")
                }
            }


            Toast.makeText(this, "Логин или пароль не верны!", Toast.LENGTH_SHORT).show();
        }

        btnSettings.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        bExit.setOnClickListener{
            Toast.makeText(this, "Выходим!", Toast.LENGTH_SHORT).show();
            finish()
        }
    }
}