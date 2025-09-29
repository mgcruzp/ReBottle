package com.example.rebottle.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.*
import com.example.rebottle.R

@Composable
fun InicioScreen(
    onStart: () -> Unit
) {
    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo arriba
            Image(
                painter = painterResource(id = R.drawable.logo_rebottle),
                contentDescription = "Logo Rebottle",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .size(350.dp) // ajusta si lo quieres más grande/pequeño
            )

            Spacer(Modifier.height(5.dp))

            // Lottie al centro
            val composition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.garbagecollection)
            )
            val progress by animateLottieCompositionAsState(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                speed = 1.0f
            )
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
                    // evita tamaños gigantes que choquen con el logo
            )

            // Botón justo debajo del Lottie
            Spacer(Modifier.height(10.dp))
            Button(
                onClick = onStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5C8C5F),
                    contentColor  = Color(0xFF1B4332)
                ),
                shape = MaterialTheme.shapes.large
            ) {
                Text(
                    "Comenzar",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 30.sp)
                )
            }

            // Espacio flexible abajo para que no quede pegado en pantallas altas
            Spacer(Modifier.height(30.dp))
        }
    }
}
