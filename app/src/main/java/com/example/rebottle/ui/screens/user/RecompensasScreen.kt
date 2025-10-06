package com.example.rebottle.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rebottle.R
import com.example.rebottle.ui.components.PrimaryButton

private val Green = Color(0xFF1B4332)
private val Mint  = Color(0xFFDCFFD6)

@Composable
fun RecompensasScreen() {
    var puntos by remember { mutableStateOf(200) }

    Surface(color = Color.White) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título centrado
            Text(
                "Recompensas",
                color = Green,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            // Tus puntos
            Text("Tus puntos", color = Green, modifier = Modifier.fillMaxWidth())

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                elevation = CardDefaults.elevatedCardElevation(2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            color = Mint,
                            shape = RoundedCornerShape(22.dp),
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .height(56.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    "$puntos pts",
                                    color = Green,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp
                                    )
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Historial de puntos",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFF6D6D6D)
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Medallas
            Text("Medallas", color = Green, modifier = Modifier.fillMaxWidth())

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                elevation = CardDefaults.elevatedCardElevation(2.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(R.drawable.medalla1), // e.g. basurita/medalla
                            contentDescription = "EcoNovato",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(84.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("EcoNovato", color = Green)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(R.drawable.medalla2), // e.g. rayo/medalla
                            contentDescription = "Primera entrega",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(84.dp)
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("Primera entrega", color = Green)
                    }
                }
            }

            Spacer(Modifier.weight(1f))

            PrimaryButton(
                text = "Redimir puntos",
                onClick = { /* TODO */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            )
        }
    }
}
