package com.example.navigationbetweenscreens

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import com.example.navigationbetweenscreens.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import  android.Manifest
import androidx.appcompat.app.AlertDialog

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var activityBinding: ActivityMainBinding

    lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var isPermissionAlreadyGranted: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(activityBinding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isPermissionAlreadyGranted){
                Toast.makeText(this, "Разрешения уже имеются", Toast.LENGTH_SHORT).show()
            } else {
                if (isGranted) {
                    Toast.makeText(this, "Разрешения получены", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Разрешения не получены", Toast.LENGTH_SHORT).show()
                }
            }
        }

        checkPermission()
    }

    private fun checkPermission() {
        val permission = getRequiredPermission()

        /*
        Странный факт, без этих разрешений запрос окна не показывается
        <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
        <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
         */
        var isPermission = checkSelfPermission(permission);
        if (isPermission == PackageManager.PERMISSION_GRANTED) {
            isPermissionAlreadyGranted = true;
        } else {
            if (shouldShowRequestPermissionRationale(permission)) {
                AlertDialog.Builder(this)
                    .setTitle("Необходимы разрешения")
                    .setMessage("Для корректной работы разрешите доступ к медиа файлом")
                    .setPositiveButton("OK") { _, _ ->
                        requestPermissionLauncher.launch(permission)
                    }
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show()
            } else {
                requestPermissionLauncher.launch(permission)
            }
        }
    }

    private fun getRequiredPermission(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    }
}