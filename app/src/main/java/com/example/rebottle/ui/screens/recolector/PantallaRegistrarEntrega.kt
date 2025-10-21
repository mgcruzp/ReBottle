package com.example.rebottle.ui.screens.recolector

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.rebottle.R
import com.example.rebottle.ui.screens.recolector.ModelEntrega
import com.example.rebottle.ui.screens.recolector.StorageCsv
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import com.example.rebottle.ui.theme.DarkGreen
import com.example.rebottle.ui.theme.LightGreen
import com.example.rebottle.ui.theme.MidGreen
import com.journeyapps.barcodescanner.IntentIntegrator
import com.journeyapps.barcodescanner.ScanIntentResult

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistrarEntrega() {
    val context = LocalContext.current

    var lugarRecoleccion by remember { mutableStateOf("") }
    var pesoRegistrado by remember { mutableStateOf("") }
    var fotoUri by remember { mutableStateOf<Uri?>(null) }
    var fotoTomada by remember { mutableStateOf(false) }

    // Lanzador para tomar foto
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            fotoTomada = true
            Toast.makeText(context, "📸 Foto capturada correctamente", Toast.LENGTH_SHORT).show()
        }
    }

    // Lanzador para escanear QR
    val qrLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val intentResult = IntentIntegrator.parseActivityResult(
            result.resultCode,
            result.data
        )
        intentResult?.contents?.let {
            lugarRecoleccion = it
            Toast.makeText(context, "QR escaneado: $it ", Toast.LENGTH_SHORT).show()
        }
    }

    // Función para crear archivo temporal de imagen
    fun crearArchivoImagen(context: Context): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir = context.getExternalFilesDir(null)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Imagen decorativa superior
        Image(
            painter = painterResource(id = R.drawable.decor_top),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 80.dp, bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            // Título
            Text(
                text = "Registro entrega",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = DarkGreen,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Campo: Lugar de recolección
            Text(
                text = "Lugar de recolección",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkGreen
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            TextField(
                value = lugarRecoleccion,
                onValueChange = { lugarRecoleccion = it },
                label = { Text("Lugar") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor =    DarkGreen
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Campo: Peso registrado
            Text(
                text = "Peso registrado (kg)",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkGreen
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            TextField(
                value = pesoRegistrado,
                onValueChange = { pesoRegistrado = it },
                label = { Text("Peso") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFF5F5F5),
                    focusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = DarkGreen
                ),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Escanear QR
            Button(
                onClick = {
                    val integrator = IntentIntegrator(context as Activity)
                    integrator.setPrompt("Escanea el código QR")
                    integrator.setOrientationLocked(true)
                    qrLauncher.launch(integrator.createScanIntent())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightGreen,
                    contentColor = DarkGreen
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("📷 Escanear QR", fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Tomar Foto
            Button(
                onClick = {
                    val fotoFile = crearArchivoImagen(context)
                    fotoUri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        fotoFile
                    )
                    fotoUri?.let { cameraLauncher.launch(it) }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightGreen,
                    contentColor = DarkGreen
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Tomar Foto", fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Registrar
            Button(
                onClick = {
                    if (lugarRecoleccion.isNotEmpty() &&
                        pesoRegistrado.isNotEmpty() &&
                        fotoTomada
                    ) {
                        val entrega = ModelEntrega(
                            lugar = lugarRecoleccion,
                            peso = pesoRegistrado.toFloat(),
                            fotoPath = fotoUri?.path ?: "",
                            fecha = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())
                        )
                        StorageCsv.guardarEntrega(context, entrega)
                        Toast.makeText(context, "¡Registro exitoso! ♻", Toast.LENGTH_LONG).show()

                        // Limpiar campos
                        lugarRecoleccion = ""
                        pesoRegistrado = ""
                        fotoTomada = false
                    } else {
                        Toast.makeText(
                            context,
                            "Por favor completa todos los campos y toma una foto",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightGreen,
                    contentColor = DarkGreen
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Registrar", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPantallaRegistrarEntrega() {
    PantallaRegistrarEntrega()
}
