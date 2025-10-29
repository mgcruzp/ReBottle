package com.example.rebottle.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rebottle.R
import com.example.rebottle.model.HistoryViewModel


data class Entrega(val fecha: String, val hora: String, val lugar: String, val estado: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(
    vm: HistoryViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    // 🔸 antes tenías un listOf(...) fijo
    val s = vm.state.collectAsState().value
    val items = s.items

    Scaffold(
        topBar = { /* igual que lo tienes */ },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = Color(0xFFDCFFD6),
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
                LazyColumn(Modifier.fillMaxWidth().padding(8.dp)) {
                    items(items) { entrega ->
                        ListItem(
                            headlineContent   = { Text("${entrega.fecha} · ${entrega.hora}") },
                            supportingContent = { Text("${entrega.lugar}\nEstado: ${entrega.estado}") }
                        )
                        Divider(color = Color(0xFF1B4332).copy(alpha = 0.2f))
                    }
                }
            }

            // resto idéntico…
            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.historial),
                    contentDescription = "Historial",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(220.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
        }
    }
}
