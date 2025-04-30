package com.example.navigationbetweenscreens.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.navigationbetweenscreens.data.GalleryRepository
import com.example.navigationbetweenscreens.data.model.Image
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: GalleryRepository
) : ViewModel() {

    private val _images = MutableLiveData<List<String>>()
    val images: LiveData<List<String>> = _images

    fun loadImages() {
        viewModelScope.launch {
            try {
                val images = repository.getImages()
                _images.value = images
            } catch (e: Exception) {
                Timber.e(e, "Error loading images")
            }
        }
    }

    suspend fun getImageDescription(imageUri: String): String? {
        return repository.getImageDescription(imageUri)
    }

    fun saveImageDescription(imageUri: String, description: String) {
        viewModelScope.launch {
            repository.saveImageDescription(imageUri, description)
        }
    }
}