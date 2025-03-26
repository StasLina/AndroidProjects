package com.example.prenotes.data.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prenotes.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {
    private val _allNotes = MutableLiveData<List<Note>>()

    val allNotes: LiveData<List<Note>> get() = _allNotes

    fun insert(note: Note) {
        Log.d("My", "insert start corutine")

        viewModelScope.launch {
            Log.d("My", "insert 1")
            noteRepository.insert(note)
            Log.d("My", "insert 2")
            loadAllNotes()
            Log.d("My", "insert ${_allNotes.value.toString()}")
        }
    }

    fun update(note: Note) {
        Log.d("My", "update corutine")
        viewModelScope.launch {
            noteRepository.update(note)
            loadAllNotes()
        }
    }

    fun delete(note: Note) {
        viewModelScope.launch {
            noteRepository.delete(note)
            loadAllNotes()
        }
    }

    fun loadAllNotes() {
        viewModelScope.launch {
            Log.d("My", "loadAllNotes 1 ${_allNotes.value.toString()}")
            val notes = noteRepository.getAllNotes()
            Log.d("My", "loadAllNotes 2 ${notes.toString()}")
            _allNotes.postValue(notes)

        }
    }
}
