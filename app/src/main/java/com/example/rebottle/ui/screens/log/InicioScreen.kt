package com.example.rebottle.ui.screens.log

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
import com.airbnb.lottie.compose.*
import com.example.rebottle.R
import com.example.rebottle.ui.components.PrimaryButton   // 👈 IMPORTANTE

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
                    .size(350.dp)
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
            )

            // Botón justo debajo del Lottie (PrimaryButton)
            Spacer(Modifier.height(10.dp))
            PrimaryButton(
                text = "Comenzar",
                onClick = onStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            )

            Spacer(Modifier.height(30.dp))
        }
    }
}
