package com.example.rebottle.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rebottle.domain.data.Role

@Composable
fun LoginScreen(
    onLogin: (Role) -> Unit,
    onGoRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var remember by remember { mutableStateOf(false) }

    Surface {
        Column(Modifier.fillMaxSize().padding(24.dp)) {
            Text("Iniciar sesión", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = email, onValueChange = { email = it },
                label = { Text("Correo") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = pass, onValueChange = { pass = it },
                label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = remember, onCheckedChange = { remember = it })
                Text("Recordarme")
                Spacer(Modifier.weight(1f))
                TextButton(onClick = { /* TODO recuperación */ }) { Text("¿Olvidó su contraseña?") }
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { onLogin(Role.USUARIO) }, // por ahora entra como USUARIO
                enabled = email.isNotBlank() && pass.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Continuar") }

            Spacer(Modifier.height(16.dp))
            TextButton(onClick = onGoRegister, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Aún no tengo cuenta")
            }
        }
    }
}
