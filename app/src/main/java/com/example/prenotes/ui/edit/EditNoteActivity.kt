package com.example.prenotes.ui.edit

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.prenotes.data.model.Note
import com.example.prenotes.data.model.NoteViewModel
import com.example.prenotes.databinding.ActivityEditNoteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditNoteActivity : AppCompatActivity() {

    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var activityBinding: ActivityEditNoteBinding
    private var noteId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityBinding = ActivityEditNoteBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)

        titleEditText = activityBinding.editNoteTitle
        contentEditText = activityBinding.editNoteContent
        saveButton = activityBinding.saveButton

        // Получаем переданную заметку из Intent
        noteId = intent.getLongExtra("NOTE_ID", -1)



        if (noteId != -1L) {
            // Загружаем заметку для редактирования
            loadNote(noteId)
        }

        saveButton.setOnClickListener {
            saveNote()
        }
        noteViewModel.loadAllNotes();
    }

    private fun loadNote(noteId: Long) {
        // Здесь вы можете загрузить заметку из базы данных
        // Например, используя ViewModel
        noteViewModel.allNotes.observe(this,{ notes ->
            NoteUpdate(notes)
        })
    }

    fun NoteUpdate(notes: List<Note>) {
        val note = notes.find { it.id == noteId }
        note?.let {
            titleEditText.setText(it.title)
            contentEditText.setText(it.content)
        }
    }

    private fun saveNote() {
        val title = titleEditText.text.toString()
        val content = contentEditText.text.toString()

        Log.d("my","saveNote");
        if (noteId != -1L) {
            // Обновляем существующую заметку
            val updatedNote = Note(id = noteId!!, title = title, content = content)
            noteViewModel.update(updatedNote)
        } else {
            // Создаем новую заметку
            val newNote = Note(title = title, content = content)
            noteViewModel.insert(newNote)
        }

        finish() // Закрываем активность после сохранения
    }
}