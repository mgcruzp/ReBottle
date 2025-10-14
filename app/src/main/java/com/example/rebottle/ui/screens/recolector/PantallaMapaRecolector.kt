package com.example.rebottle.ui.screens.recolector

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.Marker
import com.google.android.gms.maps.model.LatLng
import com.example.rebottle.ui.theme.LightGreen
import com.example.rebottle.ui.theme.DarkGreen

@Composable
fun PantallaMapaRecolector() {
    val bogota = LatLng(4.7110, -74.0721) // ubicación inicial
    val cameraPositionState = rememberCameraPositionState {
        position = com.google.android.gms.maps.model.CameraPosition.fromLatLngZoom(bogota, 12f)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreen) // fondo del verde claro de tu paleta
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker(
               // position = bogota,
                title = "Ubicación actual",
                snippet = "Recolector ReBottle"
            )
        }

        Text(
            text = "Mapa de recolección",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .background(LightGreen.copy(alpha = 0.8f))
                .padding(top = 16.dp),
            color = DarkGreen,
            style = MaterialTheme.typography.titleMedium
            )
    }
}