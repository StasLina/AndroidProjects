package com.example.prenotes

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prenotes.databinding.ActivityMainBinding
import com.example.prenotes.data.model.MainActivityModel
import com.example.prenotes.data.model.Note
import com.example.prenotes.ui.edit.EditNoteActivity
import com.example.prenotes.ui.main.NoteAdapter

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var activityBinding: ActivityMainBinding
    private val viewModel : MainActivityModel by viewModels()
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = activityBinding.rView;
        noteAdapter = NoteAdapter { note -> onNoteClicked(note) }
        recyclerView.adapter = noteAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.loadAllNotes()

        viewModel.allNotes.observe(this) { notes ->
            noteAdapter.submitList(notes)
        }

//        syncWithViewModel();
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun onNoteClicked(note: Note) {
        val intent = Intent(this, EditNoteActivity::class.java)
        intent.putExtra("NOTE_ID", note.id) // Передаем ID заметки для редактирования
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                // Обработка добавления новой заметки
                val intent = Intent(this, EditNoteActivity::class.java)
                startActivity(intent) // Открываем EditNoteActivity для добавления новой заметки
                true
            }
            R.id.action_update -> {
                // Обработка обновления заметок
                Toast.makeText(this, "Обновление заметок", Toast.LENGTH_SHORT).show()
                viewModel.loadAllNotes() // Загружаем заметки заново
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}