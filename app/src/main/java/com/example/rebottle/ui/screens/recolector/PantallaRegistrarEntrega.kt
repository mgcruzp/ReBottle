package com.example.rebottle.ui.screens.recolector

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
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rebottle.R
import com.example.rebottle.model.RegistroRecoleccionViewModel
import com.example.rebottle.nav.AppScreens
import com.example.rebottle.nav.CollectorRoute
import com.example.rebottle.ui.theme.DarkGreen
import com.example.rebottle.ui.theme.LightGreen
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaRegistrarEntrega(
    navController: NavController,
    viewModel: RegistroRecoleccionViewModel = viewModel()
) {
    val context = LocalContext.current

    var lugarRecoleccion by rememberSaveable { mutableStateOf("") }
    var pesoRegistrado by rememberSaveable { mutableStateOf("") }
    var qrEscaneado by rememberSaveable { mutableStateOf(false) }
    var photoUri by rememberSaveable { mutableStateOf<String?>(null) }

    val isLoading by viewModel.isLoading.collectAsState()

    // Recibir el resultado del scanner
    val qrResult = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<String>("qr_code")
        ?.observeAsState()

    // Cuando llegue el código QR escaneado
    LaunchedEffect(qrResult?.value) {
        qrResult?.value?.let { code ->
            qrEscaneado = true
            lugarRecoleccion = "Punto verificado: $code"
            Toast.makeText(context, "QR verificado correctamente", Toast.LENGTH_SHORT).show()
        }
    }

    // Launcher para cámara
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            Toast.makeText(context, "Foto capturada correctamente", Toast.LENGTH_SHORT).show()
        }
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
                .height(350.dp)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.FillBounds
        )

        // Contenido principal
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
                    focusedIndicatorColor = DarkGreen
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
                onClick = {navController.navigate(CollectorRoute.QR.path)},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (qrEscaneado) Color(0xFF90EE90) else LightGreen,
                    contentColor = DarkGreen
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    if (qrEscaneado) "QR Verificado" else "Escanear código QR",
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Tomar Foto
            var photoUri by remember { mutableStateOf<Uri?>(null) }

            Button(
                onClick = {
                    val photoFile = createImageFile(context)
                    val uri = androidx.core.content.FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        photoFile
                    )
                    photoUri = uri
                    cameraLauncher.launch(uri)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (photoUri != null) Color(0xFF90EE90) else LightGreen,
                    contentColor = DarkGreen
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    if (photoUri != null) "Foto Capturada" else "Tomar Foto",
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Registrar
            Button(
                onClick = {
                    if (lugarRecoleccion.isEmpty() || pesoRegistrado.isEmpty() || photoUri == null
                    ) {
                        Toast.makeText(
                            context,
                            "Por favor completa todos los campos",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    val peso = pesoRegistrado.toDoubleOrNull()
                    if (peso == null) {
                        Toast.makeText(context, "Peso inválido", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    viewModel.guardarRegistro(
                        lugar = lugarRecoleccion,
                        peso = peso,
                        fotoUri = photoUri,
                        onSuccess = {
                            Toast.makeText(context, "¡Registro exitoso!", Toast.LENGTH_LONG).show()
                            lugarRecoleccion = ""
                            pesoRegistrado = ""
                            qrEscaneado = false
                            photoUri = null
                        },
                        onError = { error ->
                            Toast.makeText(context, "Error: $error", Toast.LENGTH_LONG).show()
                        }
                    )

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightGreen,
                    contentColor = DarkGreen
                ),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = DarkGreen
                    )
                } else {
                    Text("Registrar", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

private fun createImageFile(context: android.content.Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir = context.getExternalFilesDir(null)
    return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
}