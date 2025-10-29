package com.example.rebottle.ui.screens.recolector



import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rebottle.model.LocationViewModel
import com.example.rebottle.model.MyMarker
import com.example.rebottle.model.PendingRequestsViewModel
import com.example.rebottle.model.PickupRequest
import com.example.rebottle.nav.CollectorRoute

@Composable
fun PendingRequestsScreen(vm: PendingRequestsViewModel = viewModel(), navController: NavHostController, locationVm: LocationViewModel) {
    val s by vm.state.collectAsState()

    Scaffold(
    ) { inner ->
        Box(Modifier.fillMaxSize().padding(inner)) {
            when {
                s.loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                s.error != null -> {
                    Text("Error: ${s.error}", color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center))
                }
                s.items.isEmpty() -> {
                    Text("No hay solicitudes pendientes.",
                        modifier = Modifier.align(Alignment.Center))
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(s.items, key = { it.id }) { req ->
                            PendingCard(req, navController, locationVm)
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
private fun PendingCard(req: PickupRequest, navController: NavHostController, locationVm: LocationViewModel) {
    val context = LocalContext.current


    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(req.brand.ifBlank { "Sin marca" }, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(4.dp))
            Text(req.address, style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Schedule, contentDescription = null)
                Text("${req.date}  •  ${req.time}", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(Modifier.height(8.dp))
            if (req.userName.isNotBlank()) {
                Text("Solicitante: ${req.userName}", style = MaterialTheme.typography.labelMedium)
            }
            Text("Estado: ${req.status}", style = MaterialTheme.typography.labelMedium)
        }
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.Start) {
            Button(
                onClick = {
                    val latLng = findLocation(req.address, context) ?: return@Button
                    val marker = MyMarker(latLng, req.address)
                    locationVm.replaceWith(marker)
                    Log.i("App", "LATITUD RUTA: ${latLng.latitude}, LONGITUD RUTA: ${latLng.longitude}")
                    navController.navigate(CollectorRoute.Mapa.path)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) { Text("Iniciar ruta") }
        }

    }
}

