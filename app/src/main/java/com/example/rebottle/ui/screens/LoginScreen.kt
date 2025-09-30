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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rebottle.R
import com.example.rebottle.domain.data.Role
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun LoginScreen(
    onLogin: (Role) -> Unit,
    onGoRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var remember by remember { mutableStateOf(false) }

    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_rebottle),
                contentDescription = "Logo Rebottle",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(200.dp) // ajusta si lo quieres más grande/pequeño
            )

            // Título grande, centrado y verde
            Text(
                text = "Iniciar sesión",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 30.sp,              // grande
                    color = Color(0xFF1B4332),     // verde títulos
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            // Campos
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Spacer(Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(checked = remember, onCheckedChange = { remember = it })
                Text("Recordarme")
                Spacer(Modifier.weight(1f))
                TextButton(onClick = { /* TODO: recuperación */ }) {
                    Text("¿Olvidó su contraseña?")
                }
            }

            // Imagen "basurita" justo después de los campos
            Spacer(Modifier.height(5.dp))
            Image(
                painter = painterResource(id = R.drawable.basurita),
                contentDescription = "Basurita",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(250.dp)            // ajusta si quieres más grande

            )

            // Botón Continuar grande y verde
            Spacer(Modifier.height(20.dp))
            Button(
                onClick = { onLogin(Role.USUARIO) }, // por ahora entra como USUARIO
                enabled = email.isNotBlank() && pass.isNotBlank(),
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
                    "Continuar",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 22.sp)
                )
            }

            Spacer(Modifier.height(18.dp))
            TextButton(
                onClick = onGoRegister,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Aún no tengo cuenta")
            }
        }
    }
}
