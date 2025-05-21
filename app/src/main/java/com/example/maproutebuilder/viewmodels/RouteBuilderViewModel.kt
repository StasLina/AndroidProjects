package com.example.maproutebuilder.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RouteBuilderViewModel @Inject constructor() : ViewModel() {

    private val _routePoints = MutableLiveData<List<Point>>(emptyList())
    val routePoints: LiveData<List<Point>> = _routePoints

    fun addRoutePoint(point: Point) {
        val currentPoints = _routePoints.value?.toMutableList() ?: mutableListOf()
        currentPoints.add(point)
        _routePoints.value = currentPoints
    }

    fun removeLastPoint() {
        val currentPoints = _routePoints.value?.toMutableList() ?: mutableListOf()
        if (currentPoints.isNotEmpty()) {
            currentPoints.removeAt(currentPoints.size - 1)
            _routePoints.value = currentPoints
        }
    }

    fun clearAllPoints() {
        _routePoints.value = emptyList()
    }
}