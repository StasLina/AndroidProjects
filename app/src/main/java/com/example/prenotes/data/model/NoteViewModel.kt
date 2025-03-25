package com.example.prenotes.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prenotes.data.model.Note
import com.example.prenotes.data.repository.NoteRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

open class NoteViewModel @Inject constructor(private val noteRepository: NoteRepository) : ViewModel() {
    private val _allNotes = MutableLiveData<List<Note>>()

    val allNotes: LiveData<List<Note>> get() = _allNotes

    fun insert(note: Note) {
        viewModelScope.launch {
            noteRepository.insert(note)
            loadAllNotes()
        }
    }

    fun update(note: Note) {
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
            val notes = noteRepository.getAllNotes()
            _allNotes.postValue(notes)
        }
    }
}
