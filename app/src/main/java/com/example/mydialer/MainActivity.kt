package com.example.mydialer

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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


        // Загрузка данных
        //contacts = fetchContacts()
        contacts = listOf<Contact>()

        // URL JSON-файла
        val url =
            "https://drive.google.com/u/0/uc?id=1-KO-9GA3NzSgIc1dkAsNm8Dqw0fuPxcR&export=download"

        // Загрузите JSON и распарсьте его
        loadJson(url)
        contactAdapter = ContactAdapter(contacts)

        val recyclerView: RecyclerView = findViewById(R.id.rView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = contactAdapter
        sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

        val searchEditText: EditText = findViewById(R.id.et_search)

        // Восстановливаем фильтр из SharedPreferences
        val savedFilter = sharedPreferences.getString("SEARCH_FILTER", "") ?: ""
        searchEditText.setText(savedFilter)
        filterContact(savedFilter)

        // Добавляем слушатель
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
        contactAdapter.filterContacts(filteredContacts)
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

                    // Разбор JSON
                    val contactListType = object : TypeToken<List<Contact>>() {}.type
                    val newContacts: List<Contact> = Gson().fromJson(json, contactListType)

                    // Логирование для проверки
                    newContacts.forEach {
                        Timber.v(("MainActivity Контакт: $it"))
                    }

                    // Обновление UI на главном потоке (например, заполнение RecyclerView)
                    contacts = newContacts
                    runOnUiThread {
                        // Заполните RecyclerView
                        contactAdapter.filterContacts(contacts)
                    }
                }
            }
        })

        fun fetchContacts(): List<Contact> {
            val json = "[\n" +
                    "  {\n" +
                    "    \"name\": \"(Приёмная)\",\n" +
                    "    \"phone\": \"+375 (2239) 7-17-80\",\n" +
                    "    \"type\": \"\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"(Бухгалтерия)\",\n" +
                    "    \"phone\": \"+375 (2239) 7-17-64\",\n" +
                    "    \"type\": \"\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"(Бухгалтерия)\",\n" +
                    "    \"phone\": \"+375 (2239) 7-18-08\",\n" +
                    "    \"type\": \"\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"(Юридическое бюро)\",\n" +
                    "    \"phone\": \"+375 (2239) 7-17-63\",\n" +
                    "    \"type\": \"\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"(Отдел правовой и кадровой работы)\",\n" +
                    "    \"phone\": \"+375 (2239) 7-17-93\",\n" +
                    "    \"type\": \"\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"(Отдел материально-технического снабжения)\",\n" +
                    "    \"phone\": \"+375 (2239) 7-18-12\",\n" +
                    "    \"type\": \"\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"\",\n" +
                    "    \"phone\": \"+375 44 712 36 26\",\n" +
                    "    \"type\": \"Сектор сбыта бумаги\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"(Реализация на внутренний рынок)\",\n" +
                    "    \"phone\": \"+375 (2239) 7-17-79\",\n" +
                    "    \"type\": \"Сектор сбыта бумаги\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"(Реализация на внешний рынок)\",\n" +
                    "    \"phone\": \"+375 (2239) 4-11-77\",\n" +
                    "    \"type\": \"Сектор сбыта бумаги\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"(Реализация на внутренний рынок)\",\n" +
                    "    \"phone\": \"+375 (2239) 7-18-25\",\n" +
                    "    \"type\": \"Сектор сбыта бумаги\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"\",\n" +
                    "    \"phone\": \"+375 44 580 09 70\",\n" +
                    "    \"type\": \"Сектор сбыта продукции деревообработки\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"(Реализация продукции деревообработки)\",\n" +
                    "    \"phone\": \"+375 (2239) 7-18-42\",\n" +
                    "    \"type\": \"Сектор сбыта продукции деревообработки\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"(Реализация продукции деревообработки)\",\n" +
                    "    \"phone\": \"+375 (2239) 3-64-71\",\n" +
                    "    \"type\": \"Сектор сбыта продукции деревообработки\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"\",\n" +
                    "    \"phone\": \"+375 29 352 25 20\",\n" +
                    "    \"type\": \"Реализация домов, бань, беседок, клеёного бруса\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"\",\n" +
                    "    \"phone\": \"+375 (2239) 7-18-43\",\n" +
                    "    \"type\": \"Реализация домов, бань, беседок, клеёного бруса\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"(приемная)\",\n" +
                    "    \"phone\": \"+375 (2239) 7-17-80\",\n" +
                    "    \"type\": \"Факс\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"(отдел сбыта)\",\n" +
                    "    \"phone\": \"+375 (2239) 7-17-79\",\n" +
                    "    \"type\": \"Факс\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"(отдел материально-технического снабжения)\",\n" +
                    "    \"phone\": \"+375 (2239) 7-17-82\",\n" +
                    "    \"type\": \"Факс\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"name\": \"(служба главного энергетика)\",\n" +
                    "    \"phone\": \"+375 (2239) 7-18-06\",\n" +
                    "    \"type\": \"Факс\"\n" +
                    "  }\n" +
                    "]"
            val gson = Gson()
            val listType = object : TypeToken<List<Contact>>() {}.type
            return gson.fromJson(json, listType)
        }
    }
}
