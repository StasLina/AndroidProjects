package com.example.maproutebuilder.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PointF
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.maproutebuilder.databinding.FragmentRouteBuilderBinding
import com.example.maproutebuilder.viewmodels.RouteBuilderViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.mapkit.transport.masstransit.Route
import com.yandex.mapkit.transport.masstransit.Session
import com.yandex.mapkit.transport.masstransit.TimeOptions
import com.yandex.mapkit.transport.masstransit.PedestrianRouter
import com.yandex.runtime.Error
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class RouteBuilderFragment : Fragment(), InputListener, Session.RouteListener {

    private var _binding: FragmentRouteBuilderBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView: MapView
    private lateinit var pedestrianRouter: PedestrianRouter
    private val viewModel: RouteBuilderViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) getCurrentLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRouteBuilderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Инициализация компонентов
        mapView = binding.mapView
        TransportFactory.initialize(requireContext())
        pedestrianRouter = TransportFactory.getInstance().createPedestrianRouter()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        setupMap()
        setupButtons()
        observeViewModel()
    }

    private fun setupMap() {
        val map = mapView.mapWindow.map
        map.addInputListener(this)
        map.move(
            CameraPosition(Point(55.751244, 37.618423), 10.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1f),
            null
        )
    }

    private fun setupButtons() {
        binding.btnZoomToLocation.setOnClickListener {
            checkLocationPermission()
        }

        binding.btnZoomToDefault.setOnClickListener {
            val point = Point(55.354993, 86.085805)
            mapView.mapWindow.map.move(
                CameraPosition(point, 15.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 1f),
                null
            )
        }

        binding.btnRemoveLastPoint.setOnClickListener {
            viewModel.removeLastPoint()
        }

        binding.btnClearAll.setOnClickListener {
            viewModel.clearAllPoints()
        }
    }

    private fun observeViewModel() {
        viewModel.routePoints.observe(viewLifecycleOwner) { points ->
            updateMapWithPoints(points)
            if (points.size >= 2) {
                buildPedestrianRoute(points)
            } else {
                clearRoutes()
            }
        }
    }

    private fun updateMapWithPoints(points: List<Point>) {
        val map = mapView.mapWindow.map
        map.mapObjects.clear()

        points.forEach { point ->
            map.mapObjects.addPlacemark(point).apply {
                setIconStyle(
                    IconStyle().apply {
                        setAnchor(PointF(0.5f, 1.0f))
                        setZIndex(1f)
                        setScale(1.5f)
                    }
                )
            }
        }
    }

    private fun buildPedestrianRoute(points: List<Point>) {
        if (points.size < 2) return

        val requestPoints = points.mapIndexed { index, point ->
            RequestPoint(
                point,
                RequestPointType.WAYPOINT,
                null,
                index.toString()
            )
        }

        pedestrianRouter.requestRoutes(
            requestPoints,
            TimeOptions(),
            this
        )
    }

    override fun onMasstransitRoutes(routes: MutableList<Route>) {
        if (routes.isNotEmpty()) {
            displayRoute(routes[0])
        }
    }

    override fun onMasstransitRoutesError(error: Error) {
        Timber.e("Pedestrian route error: $error")
        Toast.makeText(context, "Ошибка построения маршрута", Toast.LENGTH_SHORT).show()
    }

//    private fun displayRoute(route: Route) {
//        val map = mapView.mapWindow.map
//        val routeMapObject = map.mapObjects.addColoredPolyline(route.geometry)
//
//        routeMapObject.apply {
//            strokeWidth = 5f
//            setOutlineColor(Color.BLACK)
//            setOutlineWidth(1f)
//            setColors(listOf(Color.BLUE))
//        }
//    }

//    private fun displayRoute(route: Route) {
//        try {
//            val map = mapView.mapWindow.map
//            val geometry = route.geometry
//
//            // Очищаем предыдущие маршруты
//            map.mapObjects.clear()
//
//            // Визуализация точек маршрута
//            viewModel.routePoints.value?.forEach { point ->
//                map.mapObjects.addPlacemark(point).apply {
//                    setIconStyle(
//                        IconStyle().apply {
//                            setAnchor(0.5f, 1.0f)
//                            setZIndex(2f)
//                            setScale(1.5f)
//                        }
//                    )
//                    userData = "waypoint"
//                }
//            }
//            val polyline = map.mapObjects.addPolyline(geometry).apply {
//                strokeWidth = 6f  // Толщина линии
//                setStrokeColor(Color.parseColor("#4285F4"))  // Основной цвет
//                outlineColor = Color.WHITE  // Цвет контура
//                outlineWidth = 1.5f  // Толщина контура
//                zIndex = 1f
//                userData = "current_route"
//
//                // Для версий SDK 4.6.0+ можно добавить:
//                // isGeodesic = true  // Геодезическая линия (учет кривизны Земли)
//            }
//
//            // Автоматическое масштабирование под маршрут
//            val cameraPosition = map.cameraPosition(geometry.boundingBox)
//            map.move(
//                CameraPosition(cameraPosition.target, cameraPosition.zoom - 0.5f, 0f, 0f),
//                Animation(Animation.Type.SMOOTH, 1.5f),
//                null
//            )
//
//        } catch (e: Exception) {
//            Timber.e(e, "Error displaying route")
//            Toast.makeText(
//                context,
//                "Ошибка отображения маршрута: ${e.localizedMessage}",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//    }

    private fun displayRoute(route: Route) {
        try {
            val map = mapView.mapWindow.map
            val geometry = route.geometry

            // Очищаем предыдущие маршруты
            map.mapObjects.clear()

            // Визуализация точек маршрута
            viewModel.routePoints.value?.forEach { point ->
                map.mapObjects.addPlacemark(point).apply {
                    setIconStyle(
                        IconStyle().apply {
                            setAnchor(PointF(0.5f, 1.0f))
                            setZIndex(2f)
                            setScale(1.5f)
                        }
                    )
                    userData = "waypoint"
                }
            }

            // Создаем полилинию маршрута с новым API
            val polyline = map.mapObjects.addPolyline(geometry).apply {
                strokeWidth = 6f  // Толщина линии
                setStrokeColor(Color.parseColor("#4285F4"))  // Основной цвет
                outlineColor = Color.WHITE  // Цвет контура
                outlineWidth = 1.5f  // Толщина контура
                zIndex = 1f
                userData = "current_route"
            }

            // Автоматическое масштабирование
            val boundingBox = calculateBoundingBox(geometry.points)
//            val cameraPosition = map.cameraPosition()
//            map.move(
//                CameraPosition(cameraPosition.target, cameraPosition.zoom - 0.5f, 0f, 0f),
//                Animation(Animation.Type.SMOOTH, 1.5f),
//                null
//            )

        } catch (e: Exception) {
            Timber.e(e, "Error displaying route")
            Toast.makeText(
                context,
                "Ошибка отображения маршрута: ${e.localizedMessage}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    private fun clearRoutes() {
        mapView.mapWindow.map.mapObjects.clear()
    }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                getCurrentLocation()
            }
            else -> {
                locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val point = Point(location.latitude, location.longitude)
                        viewModel.addRoutePoint(point)
                        mapView.mapWindow.map.move(
                            CameraPosition(point, 15.0f, 0.0f, 0.0f),
                            Animation(Animation.Type.SMOOTH, 1f),
                            null
                        )
                    }
                }
        }
    }

    override fun onMapTap(map: Map, point: Point) {
        // Не используется
    }

    override fun onMapLongTap(map: Map, point: Point) {
        viewModel.addRoutePoint(point)
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

private fun calculateBoundingBox(points: List<Point>): BoundingBox {
    var minLat = Double.MAX_VALUE
    var maxLat = -Double.MAX_VALUE
    var minLon = Double.MAX_VALUE
    var maxLon = -Double.MAX_VALUE

    for (point in points) {
        minLat = minOf(minLat, point.latitude)
        maxLat = maxOf(maxLat, point.latitude)
        minLon = minOf(minLon, point.longitude)
        maxLon = maxOf(maxLon, point.longitude)
    }

    return BoundingBox(
        Point(minLat, minLon),
        Point(maxLat, maxLon)
    )
}
