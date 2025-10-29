package com.example.rebottle.ui.screens.user

import com.example.rebottle.model.PickupRequest
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.rebottle.model.MyPendingRequestsViewModel

@Composable
fun MyPendingRequestsScreen(vm: MyPendingRequestsViewModel = viewModel()) {
    val s by vm.state.collectAsState()

    Scaffold(

    ) { inner ->
        Box(Modifier.fillMaxSize().padding(inner)) {
            when {
                s.loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                s.error != null -> Text("Error: ${s.error}", color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center))
                s.items.isEmpty() -> Text("No tienes solicitudes activas.",
                    modifier = Modifier.align(Alignment.Center))
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(s.items, key = { it.id }) { req ->
                            MyPendingCard(req)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MyPendingCard(req: PickupRequest) {
    Card(Modifier.fillMaxWidth()) {
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
    }
}
