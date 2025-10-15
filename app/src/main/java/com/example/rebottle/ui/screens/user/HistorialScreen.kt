package com.example.rebottle.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rebottle.R

data class Entrega(val fecha: String, val hora: String, val lugar: String, val estado: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen() {
    val items = listOf(
        Entrega("12/09/2025", "10:30", "Transversal 23 #97-23", "Entregado"),
        Entrega("09/09/2025", "15:15", "Calle 100 #8A", "Programado"),
        Entrega("05/09/2025", "08:45", "Cra 15 #80-20", "Cancelado"),
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Historial de recolecciones",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors( // 👈 usa la variante center
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF1B4332)
                )
            )
        },
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
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp) // opcional
            ) {
                LazyColumn(Modifier.fillMaxWidth().padding(8.dp)) {
                    items(items) { entrega ->
                        ListItem(
                            headlineContent   = { Text("${entrega.fecha} · ${entrega.hora}") },
                            supportingContent = { Text("${entrega.lugar}\nEstado: ${entrega.estado}") }
                        )
                        Divider(color = Color(0xFF1B4332).copy(alpha = 0.2f)) // opcional
                    }
                }
            }

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
