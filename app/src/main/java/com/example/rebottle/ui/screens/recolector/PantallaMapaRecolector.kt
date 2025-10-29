package com.example.rebottle.ui.screens.recolector

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import android.os.Looper
import android.util.Log
import android.webkit.WebView.findAddress
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rebottle.api.DirectionsRepo
import com.example.rebottle.model.LocationState
import com.example.rebottle.model.LocationViewModel
import com.example.rebottle.model.MyMarker
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.BuildConfig
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.util.Date
import java.util.Properties
import com.example.rebottle.R





class MyLocation(val date: Date, val latitude: Double, val longitude: Double) {
    fun toJSON(): JSONObject {
        return JSONObject().apply {
            put("latitude", latitude)
            put("longitude", longitude)
            put("date", date.time)
        }
    }
}


@Composable
fun LocationScreen(vm: LocationViewModel = viewModel()) {
    val context = LocalContext.current
    val state by vm.state.collectAsState()
    val historial = remember { mutableStateListOf<LatLng>() }

    val locationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationRequest = remember {
        LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
            .setWaitForAccurateLocation(true)
            .build()
    }

    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc = result.lastLocation ?: return
                val curr = LatLng(loc.latitude, loc.longitude)
                if (historial.isNotEmpty()) {
                    val previo = historial.last()
                    //val dist = distance(previo.latitude, previo.longitude, curr.latitude, curr.longitude)
                    val results = FloatArray(1)
                    android.location.Location.distanceBetween(
                        previo.latitude,
                        previo.longitude,
                        curr.latitude,
                        curr.longitude,
                        results
                    )
                    val metros = results[0]
                    if (metros > 30f) {
                        writeJSONObject(result, context)
                        Log.d("DIST", ">> Escribí JSON (d>${30f})")
                    }
                }
                historial.add(curr)
                Log.i("LocationApp", "lat=${loc.latitude}, lon=${loc.longitude}")
                vm.update(loc.latitude, loc.longitude)
            }
        }
    }


    var hasPermission by remember {
        mutableStateOf(
            ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val requestPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasPermission = granted
        if (granted) {
            startLocationUpdatesIfGranted(
                locationClient,
                locationRequest,
                locationCallback,
                context
            )
        }
    }


    DisposableEffect(hasPermission) {
        if (hasPermission) {
            startLocationUpdatesIfGranted(
                locationClient,
                locationRequest,
                locationCallback,
                context
            )
        }
        onDispose {
            locationClient.removeLocationUpdates(locationCallback)
        }
    }

    if (!hasPermission) {
        Button(onClick = { requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION) }) {
            Text("Conceder permiso de ubicación")
        }
    }
    else{
        PantallaMapaRecolector(vm)
    }
}


@Composable
fun PantallaMapaRecolector(viewModel: LocationViewModel) {

    var place by remember { mutableStateOf("") }
    val sensorManager =
        LocalContext.current.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    val LocActual = LatLng(state.latitude, state.longitude)
    val lightMapStyle = MapStyleOptions.loadRawResourceStyle(context, R.raw.default_map)
    val darkMapStyle = MapStyleOptions.loadRawResourceStyle(context, R.raw.night_map)
    var currentMapStyle by remember { mutableStateOf(lightMapStyle) }


    val sensorListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            if (event?.sensor?.type == Sensor.TYPE_LIGHT) {
                val lux = event.values[0]
                Log.i("MapApp", lux.toString())
                currentMapStyle = if (lux < 3000) darkMapStyle else lightMapStyle
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
// Handle accuracy changes if needed
        }
    }


    DisposableEffect(Unit) {
        sensorManager.registerListener(
            sensorListener,
            lightSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
        onDispose {
            sensorManager.unregisterListener(sensorListener)
        }
    }


    val actualMarkerState = rememberUpdatedMarkerState(position = LocActual)

    val searchMarker = rememberUpdatedMarkerState()
    var searchMarkerTitle = remember { mutableStateOf("") }

    val cameraPositionState = key(LocActual) {
        rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(LocActual, 18f)
        }
    }
    val markers by viewModel.markers.collectAsState()
    var longClickMarker = rememberUpdatedMarkerState(position = LocActual)
    var longClickMarkerTitle by remember {mutableStateOf("")}
    val routePoints = remember { mutableStateListOf<LatLng>() }
    val scope = rememberCoroutineScope()

    val directionsKey = context.getString(R.string.google_directions_key)

    val pendingDest by viewModel.pendingRouteTo.collectAsState()

    LaunchedEffect(pendingDest) {
        val dest = pendingDest ?: return@LaunchedEffect
        if (directionsKey.isBlank()) {
            Toast.makeText(context, "API key vacía.", Toast.LENGTH_SHORT).show()
            return@LaunchedEffect
        }
        val pts = withContext(Dispatchers.IO) {
            DirectionsRepo.fetchRoutePoints(
                origin = LocActual,
                dest = dest,
                apiKey = directionsKey
            )
        }
        routePoints.clear()
        routePoints.addAll(pts)
        if (pts.isEmpty()) {
            Toast.makeText(context, "No se pudo obtener la ruta.", Toast.LENGTH_SHORT).show()
        }

        viewModel.clearPendingRoute()

    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapStyleOptions = currentMapStyle),
            onMapLongClick = { position ->
                val address = findAddress(position, context)
                address?.let{
                    longClickMarker.position = position
                    longClickMarkerTitle = address
                    cameraPositionState.position =
                        CameraPosition.fromLatLngZoom(position, 18F)
                    viewModel.addMarker(MyMarker(position, place))
                    val results = FloatArray(1)
                    android.location.Location.distanceBetween(
                        LocActual.latitude,
                        LocActual.longitude,
                        position.latitude,
                        position.longitude,
                        results
                    )
                    val metros = results[0]
                    Toast.makeText(context, "Estas a : $metros metros de $address", Toast.LENGTH_SHORT).show()


                    scope.launch {
                        if (directionsKey.isBlank()) {
                            Toast.makeText(context, "API key vacía.", Toast.LENGTH_SHORT).show()
                            return@launch
                        }
                        val pts = withContext(Dispatchers.IO) {
                            DirectionsRepo.fetchRoutePoints(
                                origin = LocActual,
                                dest = position,
                                apiKey = directionsKey
                            )
                        }
                        routePoints.clear()
                        routePoints.addAll(pts)
                        if (pts.isEmpty()) {
                            Toast.makeText(context, "No se pudo obtener la ruta.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

        )
        {
            Marker(
                state = actualMarkerState,
                title = "Actual",
                snippet = "Posición Actual"
            )
            markers.forEach {
                Marker(
                    state = rememberUpdatedMarkerState(it.position),
                    title = it.title,
                )


                }
            if (routePoints.isNotEmpty()) {
                Polyline(points = routePoints.toList(), width = 12f)
            }
        }

        TextField(
            value = place,
            onValueChange = {place = it},
            label = {Text("Place")},
            modifier = Modifier.fillMaxWidth()
                .padding(start = 16.dp, top = 48.dp, end = 16.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(
                onSearch = {
                    Log.i("MapApp", place)
                    val location = findLocation(place, context)
                    location?.let{
                        searchMarker.position = location
                        searchMarkerTitle.value =place
                        cameraPositionState.position =
                            CameraPosition.fromLatLngZoom(location, 18F)
                        viewModel.addMarker(MyMarker(location, place))

                        val results = FloatArray(1)
                        android.location.Location.distanceBetween(
                            LocActual.latitude,
                            LocActual.longitude,
                            location.latitude,
                            location.longitude,
                            results
                        )
                        val metros = results[0]
                        Toast.makeText(context, "Estas a : $metros metros de $place", Toast.LENGTH_SHORT).show()

                        scope.launch {
                            if (directionsKey.isBlank()) {
                                Toast.makeText(context, "API key vacía.", Toast.LENGTH_SHORT).show()
                                return@launch
                            }
                            val pts = withContext(Dispatchers.IO) {
                                DirectionsRepo.fetchRoutePoints(
                                    origin = LocActual,
                                    dest = location,
                                    apiKey = directionsKey
                                )
                            }
                            routePoints.clear()
                            routePoints.addAll(pts)
                            if (pts.isEmpty()) {
                                Toast.makeText(context, "No se pudo obtener la ruta.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            ),
            shape = RoundedCornerShape(16.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorResource(R.color.transparentFocused),
                unfocusedContainerColor = colorResource(R.color.transparentUnfocused),
                focusedLabelColor = colorResource(R.color.black),
                unfocusedLabelColor = colorResource(R.color.transparentWhite),
                focusedTextColor = Color.Black
            )
        )
    }
}




private fun startLocationUpdatesIfGranted(
    client: FusedLocationProviderClient,
    request: LocationRequest,
    callback: LocationCallback,
    context: android.content.Context
) {
    if (ActivityCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        client.requestLocationUpdates(request, callback, Looper.getMainLooper())
    }
}


fun writeJSONObject(result: LocationResult, baseContext: Context ) {
    val currentLocation = result.lastLocation ?: return
    val file = File(baseContext.getExternalFilesDir(null), "locations.json")


    val jsonArray = if (file.exists() && file.length() > 0) {
        try {
            JSONArray(file.readText())
        } catch (_: Exception) { JSONArray() }
    } else JSONArray()

    val myLocation = MyLocation(Date(), currentLocation.latitude, currentLocation.longitude)
    jsonArray.put(myLocation.toJSON())


    file.writeText(jsonArray.toString())
    Log.i("LOCATION", "File modified at path: $file")
}



fun findAddress (location : LatLng, context: Context):String?{
    var geocoder = Geocoder(context)
    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 2)
    if(addresses != null && !addresses.isEmpty()){
        val addr = addresses.get(0)
        val locname = addr.getAddressLine(0)
        return locname
    }
    return null
}
fun findLocation(address : String, context: Context):LatLng?{
    var geocoder = Geocoder(context)
    val addresses = geocoder.getFromLocationName(address, 2)
    if(addresses != null && !addresses.isEmpty()){
        val addr = addresses.get(0)
        val location = LatLng(addr.
        latitude, addr.
        longitude)
        return location
    }
    return null
}




