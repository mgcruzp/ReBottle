package com.example.rebottle.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class Entrega(val fecha: String, val hora: String, val lugar: String, val estado: String)

@Composable
fun HistorialScreen() {
    val items = listOf(
        Entrega("12/09/2025", "10:30", "Transversal 23 #97-23", "Entregado"),
        Entrega("09/09/2025", "15:15", "Calle 100 #8A", "Programado"),
        Entrega("05/09/2025", "08:45", "Cra 15 #80-20", "Cancelado"),
    )

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Historial de solicitudes", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(12.dp))

        ElevatedCard(Modifier.fillMaxWidth()) {
            LazyColumn(Modifier.fillMaxWidth().padding(8.dp)) {
                items(items) { it ->
                    ListItem(
                        headlineContent = { Text("${it.fecha} · ${it.hora}") },
                        supportingContent = { Text("${it.lugar}\nEstado: ${it.estado}") }
                    )
                    Divider()
                }
            }
        }
    }
}
