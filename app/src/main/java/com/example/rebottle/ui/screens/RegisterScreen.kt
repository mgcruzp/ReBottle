package com.example.rebottle.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rebottle.domain.data.Role
import com.example.rebottle.ui.components.RoleSelector

@Composable
fun RegisterScreen(
    onRegistered: (Role) -> Unit,
    onGoLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var role by remember { mutableStateOf<Role?>(null) }

    Surface {
        Column(Modifier.fillMaxSize().padding(24.dp)) {
            Text("Crear cuenta", style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(value = pass, onValueChange = { pass = it }, label = { Text("Contraseña") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))

            RoleSelector(selected = role, onSelect = { role = it })

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { role?.let(onRegistered) },
                enabled = role != null && name.isNotBlank() && email.isNotBlank() && pass.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) { Text("Continuar") }

            Spacer(Modifier.height(8.dp))
            TextButton(onClick = onGoLogin, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Ya tengo cuenta")
            }
        }
    }
}
