package com.example.rebottle.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun InicioUsuarioScreen(
    onProgramar: () -> Unit,
    onRecompensas: () -> Unit
) {
    Surface(color = Color.White) {
        Box(Modifier.fillMaxSize()) {

//            Image(
//                painter = painterResource(id = R.drawable.decor_top),
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(220.dp)
//                    .align(Alignment.TopCenter),
//                contentScale = ContentScale.FillBounds
//            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            )  {
                Spacer(Modifier.height(8.dp))
                Text(
                    "Hola usuario!",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1B4332)
                    )
                )

                Spacer(Modifier.height(8.dp))
                Image(
                    painter = painterResource(R.drawable.logo_rebottle),
                    contentDescription = "Rebottle",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(180.dp)
                )

                Spacer(Modifier.height(16.dp))

                ActionCard(
                    imageRes = R.drawable.programar,
                    title = "Programar recoleccion",
                    onClick = onProgramar,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(16.dp))

                ActionCard(
                    imageRes = R.drawable.recompensas,
                    title = "Recompensas",
                    onClick = onRecompensas,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun ActionCard(
    imageRes: Int,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.height(190.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White,
            contentColor = Color(0xFF1B4332)
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = title,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(110.dp)
            )
            Text(title, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
