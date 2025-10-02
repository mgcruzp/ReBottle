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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rebottle.R
import com.example.rebottle.domain.data.Role
import com.example.rebottle.ui.components.PrimaryButton
import androidx.compose.foundation.text.KeyboardOptions

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
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_rebottle),
                contentDescription = "Logo Rebottle",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(200.dp)
            )

            // Título
            Text(
                text = "Iniciar sesión",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 30.sp,
                    color = Color(0xFF1B4332),
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
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
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

            // Imagen “basurita”
            Spacer(Modifier.height(5.dp))
            Image(
                painter = painterResource(id = R.drawable.basurita),
                contentDescription = "Basurita",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(250.dp)
            )

            // Botón Continuar (PrimaryButton)
            Spacer(Modifier.height(20.dp))
            PrimaryButton(
                text = "Continuar",
                enabled = email.isNotBlank() && pass.isNotBlank(),
                onClick = { onLogin(Role.USUARIO) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            )

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
