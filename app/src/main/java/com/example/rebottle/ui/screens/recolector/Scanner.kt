package com.example.rebottle.ui.screens.recolector

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions

@Composable
fun ScannerScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as Activity
    var scanResult by remember { mutableStateOf("Presiona el botón para escanear") }

    // Launcher del escáner (oficial de JourneyApps)
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
        Button(
            onClick = {
                // Pedir permiso de cámara si no está concedido
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                    != android.content.pm.PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        activity,
                        arrayOf(android.Manifest.permission.CAMERA),
                        101
                    )
                } else {
                    val options = ScanOptions()
                    options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                    options.setPrompt("Escanea el código QR")
                    options.setBeepEnabled(true)
                    options.setOrientationLocked(true)
                    barcodeLauncher.launch(options)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text("Iniciar Escaneo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            )
        ) {
            Text("Volver")
        }
    }
}
