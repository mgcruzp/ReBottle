package com.example.rebottle.ui.screens.recolector

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rebottle.ui.theme.DarkGreen
import com.example.rebottle.ui.theme.LightGreen
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun ScannerScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as Activity
    var scanResult by remember { mutableStateOf("Presiona el botón para escanear") }

    // ✅ Launcher del escáner
    val barcodeLauncher = rememberLauncherForActivityResult(ScanContract()) { result ->
        if (result.contents != null) {
            scanResult = "Código escaneado: ${result.contents}"
            Toast.makeText(context, "QR verificado", Toast.LENGTH_SHORT).show()

            // Guardar el valor en la pantalla anterior
            navController.previousBackStackEntry
                ?.savedStateHandle
                ?.set("qr_code", result.contents)

            navController.popBackStack()
        } else {
            Toast.makeText(context, "Escaneo cancelado", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(scanResult)
        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Botón corregido
        Button(
            onClick = {
                val options = ScanOptions().apply {
                    setPrompt("Escanea el código QR")
                    setBeepEnabled(true)
                    setOrientationLocked(true)
                }
                barcodeLauncher.launch(options)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightGreen,
                contentColor = DarkGreen
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Escanear", fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 24.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LightGreen,
                contentColor = DarkGreen
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Volver", fontWeight = FontWeight.SemiBold)
        }
    }
}
