package com.example.mydialer

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.IOException
import androidx.core.widget.addTextChangedListener

class MainActivity : AppCompatActivity() {
    private lateinit var contactAdapter: ContactAdapter
    private lateinit var contacts: List<Contact>
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Timber.plant(Timber.DebugTree())

        contacts = listOf<Contact>()

        val url =
            "https://drive.google.com/u/0/uc?id=1-KO-9GA3NzSgIc1dkAsNm8Dqw0fuPxcR&export=download"

        loadJson(url)
        contactAdapter = ContactAdapter()

        val recyclerView: RecyclerView = findViewById(R.id.rView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = contactAdapter
        sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

        val searchEditText: EditText = findViewById(R.id.et_search)

        val savedFilter = sharedPreferences.getString("SEARCH_FILTER", "") ?: ""
        searchEditText.setText(savedFilter)
        filterContact(savedFilter)

        searchEditText.addTextChangedListener()  {
            val query = searchEditText.text.toString()

            sharedPreferences.edit()
                .putString("SEARCH_FILTER", query)
                .apply()

            filterContact(query)
        }
    }

    private fun filterContact(pattern: String) : List<Contact>{
        val filteredContacts = if (pattern.isEmpty()) {
            contacts
        } else {
            contacts.filter {
                it.name.contains(pattern, ignoreCase = true)
                        ||it.phone.contains(pattern, ignoreCase = true) }
        }
        contactAdapter.submitList(filteredContacts)
        return  filteredContacts
    }

    private fun loadJson(url: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Timber.v("MainActivity Ошибка загрузки JSON", e)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                response.body?.let { responseBody ->
                    val json = responseBody.string()

                    val contactListType = object : TypeToken<List<Contact>>() {}.type
                    val newContacts: List<Contact> = Gson().fromJson(json, contactListType)

                    newContacts.forEach {
                        Timber.v(("MainActivity Контакт: $it"))
                    }

                    contacts = newContacts
                    runOnUiThread {
                        contactAdapter.submitList(contacts)
                    }
                }
            }
        })
    }
}
