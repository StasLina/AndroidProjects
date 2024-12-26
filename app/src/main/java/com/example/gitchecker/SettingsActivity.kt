package com.example.gitchecker

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import com.example.gitchecker.databinding.ActivityConnectionSettingsBinding
import com.example.gitchecker.models.AppData
import kotlinx.coroutines.launch

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
    private val appData: AppData by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Связываем макет с данными
        binding = ActivityConnectionSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = settingsViewmodel
        binding.lifecycleOwner = this // Убедитесь, что lifecycleOwner установлен


        val btnSettingsSave: Button = findViewById(R.id.SaveClick)
        val bExit: ImageView = findViewById(R.id.Close)

        btnSettingsSave.setOnClickListener {

            lifecycleScope.launch {
                // Логируем попытку сохранить логин
                settingsViewmodel.protocol.value?.takeIf { it.isNotEmpty() }?.let {
                    Log.d("SettingsActivity", "Saving protocol: $it")
                    appData.dataHard.saveProtocol(it)
                } ?: Log.d("SettingsActivity", "protocol is empty or null, not saving.")

                // Логируем попытку сохранить пароль
                settingsViewmodel.address.value?.takeIf { it.isNotEmpty() }?.let {
                    Log.d("SettingsActivity", "Saving address: $it")
                    appData.dataHard.saveAddress(it)
                } ?: Log.d("SettingsActivity", "address is empty or null, not saving.")
            }

            Toast.makeText(this, "Настройки сохранены", Toast.LENGTH_SHORT).show();

        }

        bExit.setOnClickListener {
            Toast.makeText(this, "Выходим!", Toast.LENGTH_SHORT).show();
            finish()
        }

        lifecycleScope.launch {
            appData.dataHard.address.collect { address ->
                address?.takeIf { it.isNotEmpty() }?.let {
                    Log.d("LoginActivity", "finded login: $it")
                    settingsViewmodel.address.value = it
                }
            }
        }

        lifecycleScope.launch{
            appData.dataHard.protocol.collect { protocol ->
                protocol?.takeIf { it.isNotEmpty() }?.let {
                    Log.d("LoginActivity", "finded password: $it")
                    settingsViewmodel.protocol.value = it
                }
            }
        }

    }
}