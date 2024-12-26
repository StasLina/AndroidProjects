package com.example.gitchecker.models

import android.content.Context
import android.location.Address
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

// Делегат для доступа к DataStore
private val Context.dataStore by preferencesDataStore(name = "app_data")

class HardData(private val context: Context) {

    companion object {
        private val PROTOCOL_KEY = stringPreferencesKey("protocol")
        private val LOGIN_KEY = stringPreferencesKey("login")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val ADDRESS_KEY = stringPreferencesKey("address")

        @Volatile
        private var instance: HardData? = null

        fun getInstance(context: Context): HardData {
            return instance ?: synchronized(this) {
                instance ?: HardData(context.applicationContext).also { instance = it }
            }
        }
    }

    // Чтение данных из DataStore
    val protocol: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PROTOCOL_KEY]  // Возвращаем значение по ключу
    }

    val address: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[ADDRESS_KEY]  // Возвращаем значение по ключу
    }

    val login: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[LOGIN_KEY]  // Возвращаем значение по ключу
    }

    val password: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[PASSWORD_KEY]  // Возвращаем значение по ключу
    }

    // Запись данных в DataStore
    suspend fun saveProtocol(protocol: String) {
        context.dataStore.edit { preferences ->
            preferences[PROTOCOL_KEY] = protocol  // Сохраняем значение по ключу
        }
    }

    suspend fun saveAddress(address: String) {
        context.dataStore.edit { preferences ->
            preferences[ADDRESS_KEY] = address
        }
    }

    // Аналогично для других данных (например, login и password)
    suspend fun saveLogin(login: String) {
        context.dataStore.edit { preferences ->
            preferences[LOGIN_KEY] = login
        }
    }

    suspend fun savePassword(password: String) {
        context.dataStore.edit { preferences ->
            preferences[PASSWORD_KEY] = password
        }
    }


}
