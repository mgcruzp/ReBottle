package com.example.rebottle.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.rebottle.R

@Composable
fun InicioUsuarioScreen(
    onProgramar: () -> Unit,
    onRecompensas: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Image(
            painter = painterResource(R.drawable.logo_rebottle),
            contentDescription = "Rebottle",
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(180.dp)
        )

        Spacer(Modifier.height(8.dp))

        // Tarjetas acciones
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
//            ActionCard(
//                //iconRes = R.drawable.ic_calendar, // si no tienes, usa un png o reemplaza por material Icon
//                title = "Programar\nsolicitud",
//                onClick = onProgramar,
//                modifier = Modifier.weight(1f)
//            )
//            ActionCard(
//                iconRes = R.drawable.ic_trophy,
//                title = "Recompensas",
//                onClick = onRecompensas,
//                modifier = Modifier.weight(1f)
//            )
        }

        // Blob decor abajo (opcional)
        Spacer(Modifier.weight(1f))

    }
}

@Composable
private fun ActionCard(
    iconRes: Int,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.height(140.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = title,
                modifier = Modifier.size(56.dp),
                contentScale = ContentScale.Fit
            )
            Text(title, style = MaterialTheme.typography.titleMedium)
        }
    }
}
