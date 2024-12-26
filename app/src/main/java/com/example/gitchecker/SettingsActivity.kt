package com.example.gitchecker

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gitchecker.databinding.ActivityConnectionSettingsBinding

class SettingsViewModel() : ViewModel() {
    val address  = MutableLiveData<String>()
//    val address: LiveData<String> get() = _login
//
//    fun setAddress(newAddress: String) {
//        _address.value = newAddress
//    }

    val protocol = MutableLiveData<String>()
//    val protocol: LiveData<String> get() = _protocol
//
//    fun setProtocol(newProtocol: String) {
//        _protocol.value = newProtocol
//    }
}
class SettingsActivity : AppCompatActivity() {
    private val settingsViewmodel : SettingsViewModel by viewModels()
    private lateinit var binding: ActivityConnectionSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Связываем макет с данными
        binding = ActivityConnectionSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = settingsViewmodel
        binding.lifecycleOwner = this // Убедитесь, что lifecycleOwner установлен

        // Связываем макет с Activity
        setContentView(R.layout.activity_connection_settings)



        val btnSettingsSave: Button = findViewById(R.id.SaveClick)
        val bExit: ImageView = findViewById(R.id.Close)

        btnSettingsSave.setOnClickListener {
            Toast.makeText(this, "Настройки сохранены", Toast.LENGTH_SHORT).show();
        }

        bExit.setOnClickListener {
            Toast.makeText(this, "Выходим!", Toast.LENGTH_SHORT).show();
            finish()
        }
    }
}