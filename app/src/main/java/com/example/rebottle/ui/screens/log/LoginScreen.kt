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

private val Green = Color(0xFF1B4332)
private val Mint  = Color(0xFFDCFFD6)

@Composable
fun LoginScreen(
    onLogin: (Role) -> Unit,
    onGoRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var remember by remember { mutableStateOf(false) }

    Surface(color = Color.White) {
        Box(Modifier.fillMaxSize()) {

            Image(
                painter = painterResource(id = R.drawable.decor_top), // 👈 tu PNG verde
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .align(Alignment.TopCenter),
                contentScale = ContentScale.FillWidth
            )

            // Contenido
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo
                Spacer(Modifier.height(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.logo_rebottle),
                    contentDescription = "Logo Rebottle",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(200.dp)
                )

                // Título
                Text(
                    text = "Iniciar sesión",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 30.sp,
                        color = Green,
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
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Green,
                        unfocusedBorderColor = Mint,
                        cursorColor = Green,
                        focusedLabelColor = Green
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
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Green,
                        unfocusedBorderColor = Mint,
                        cursorColor = Green,
                        focusedLabelColor = Green
                    )
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
                        Text("¿Olvidó su contraseña?", color = Green)
                    }
                }

                Spacer(Modifier.height(6.dp))
                Image(
                    painter = painterResource(id = R.drawable.basurita),
                    contentDescription = "Basurita",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(230.dp)
                )

                Spacer(Modifier.height(16.dp))
                PrimaryButton(
                    text = "Continuar",
                    enabled = email.isNotBlank() && pass.isNotBlank(),
                    onClick = { onLogin(Role.USUARIO) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                )

                Spacer(Modifier.height(14.dp))
                TextButton(onClick = onGoRegister) {
                    Text(
                        text = "Aún no tengo cuenta",
                        color = Green,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}
