package com.example.rebottle.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rebottle.R
import com.example.rebottle.model.RegistroRecoleccionViewModel
import com.example.rebottle.ui.theme.DarkGreen
import com.example.rebottle.ui.theme.Typography

@Composable
fun PantallaRecolector(viewModel: RegistroRecoleccionViewModel = viewModel()) {

    var totalEntregas by remember { mutableStateOf(0) }
    var totalPeso by remember { mutableStateOf(0.0) }
    var ganancias by remember { mutableStateOf(0.0) }
    var isLoading by remember { mutableStateOf(true) }

    // 🔄 Llamamos a Firestore cuando se abre la pantalla
    LaunchedEffect(Unit) {
        viewModel.obtenerTotales(
            onResult = { entregas, peso ->
                totalEntregas = entregas
                totalPeso = peso
                ganancias = peso * 600 // 💰 ejemplo: $600 por kg
                isLoading = false
            },
            onError = {
                isLoading = false
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.decor_top),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.FillBounds
        )

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF1B4332))
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Hola Recolector!",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1B4332)
                    )
                )
                Spacer(modifier = Modifier.height(24.dp))
                Image(
                    painter = painterResource(id = R.drawable.logo_rebottle),
                    contentDescription = "Logo Rebottle",
                    modifier = Modifier.size(250.dp)
                )
                Text(
                    text = "Este es tu resumen semanal",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1B4332)
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFB8F8AD)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Entregas finalizadas:",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1B4332)
                            )
                        )
                        Text(
                            text = "$totalEntregas",
                            style = Typography.headlineMedium.copy(
                                color = DarkGreen,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(39.dp))
                        Text(
                            text = "Peso total entregado:",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1B4332)
                            )
                        )
                        Text(
                            text = "${"%.2f".format(totalPeso)} kg",
                            style = Typography.headlineMedium.copy(
                                color = DarkGreen,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(39.dp))
                        Text(
                            text = "Ganancias totales:",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1B4332)
                            )
                        )
                        Text(
                            text = "$${"%,.0f".format(ganancias)}",
                            style = Typography.headlineMedium.copy(
                                color = DarkGreen,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaRecolector() {
    PantallaRecolector()
}
