package com.example.rebottle.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rebottle.ui.components.PrimaryButton

@Composable
fun RecompensasScreen() {
    var puntos by remember { mutableStateOf(200) }

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Recompensas", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))

        ElevatedCard(Modifier.fillMaxWidth()) {
            Box(Modifier.fillMaxWidth().padding(24.dp)) {
                Text("$puntos pts", style = MaterialTheme.typography.headlineMedium)
            }
        }

        Spacer(Modifier.height(16.dp))

        ElevatedCard(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Medallas", style = MaterialTheme.typography.titleMedium)
                Text("🏅 EcoNovato")
                Text("⚡ Primera entrega")
            }
        }

        Spacer(Modifier.weight(1f))
        PrimaryButton(
            text = "Redimir puntos",
            onClick = { /* TODO */ },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        )
    }
}
