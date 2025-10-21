package com.example.rebottle.ui.screens.recolector

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@Composable
fun PantallaMapaRecolector(
    puntosRecoleccion: List<LatLng> = emptyList()
) {
    val context = LocalContext.current
    var permisoConcedido by remember { mutableStateOf(false) }

    // Lanzador para pedir permiso de ubicación
    val permisoUbicacionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        permisoConcedido = isGranted
    }

    // Pedimos el permiso al iniciar la pantalla
    LaunchedEffect(Unit) {
        val fineLocationGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!fineLocationGranted) {
            permisoUbicacionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            permisoConcedido = true
        }
    }

    // Si aún no se concedió el permiso, mostramos un indicador de carga
    if (!permisoConcedido) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator()
        }
        return
    }

    // Recuperar las coordenadas guardadas del usuario (por ejemplo, desde ProgramarSolicitudScreen)
    val prefs = context.getSharedPreferences("ReBottlePrefs", Context.MODE_PRIVATE)
    val lat = prefs.getFloat("latitud", 0f)
    val lng = prefs.getFloat("longitud", 0f)

    // Si no hay coordenadas guardadas, usamos una ubicación por defecto (Bogotá)
    val userLocation = if (lat != 0f && lng != 0f) {
        LatLng(lat.toDouble(), lng.toDouble())
    } else {
        LatLng(4.7110, -74.0721) // Bogotá
    }

    // Estado de la cámara del mapa
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(userLocation, 14f)
    }

    // Configuración y render del mapa
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        uiSettings = MapUiSettings(
            zoomControlsEnabled = true,
            compassEnabled = true,
            myLocationButtonEnabled = true
        ),
        properties = MapProperties(
            isMyLocationEnabled = permisoConcedido
        )
    ) {
        // Marcador principal de la ubicación guardada
        Marker(
            state = MarkerState(position = userLocation),
            title = "Ubicación programada",
            snippet = "Entrega del usuario"
        )

        // Puntos adicionales de recolección, si existen
        puntosRecoleccion.forEachIndexed { index, punto ->
            Marker(
                state = MarkerState(position = punto),
                title = "Punto ${index + 1}",
                snippet = "Recolección programada"
            )
        }
    }
}
