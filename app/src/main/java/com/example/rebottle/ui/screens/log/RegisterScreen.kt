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
import com.example.rebottle.ui.components.RoleSelector
import com.example.rebottle.ui.components.PrimaryButton
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun RegisterScreen(
    onRegistered: (Role) -> Unit,
    onGoLogin: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    var role by remember { mutableStateOf<Role?>(null) }

    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
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

            Text(
                text = "Crear cuenta",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 30.sp,
                    color = Color(0xFF1B4332),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Spacer(Modifier.height(12.dp))
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

            Spacer(Modifier.height(16.dp))

            Spacer(Modifier.height(16.dp))
            RoleSelector(selected = role, onSelect = { role = it })

            Spacer(Modifier.height(24.dp))
            PrimaryButton(
                text = "Continuar",
                enabled = role != null && name.isNotBlank() && email.isNotBlank() && pass.isNotBlank(),
                onClick = { role?.let(onRegistered) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            )

            Spacer(Modifier.height(18.dp))
            TextButton(onClick = onGoLogin, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text(
                    "Ya tengo cuenta",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 15.sp,
                        color = Color(0xFF1B4332),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                    )
            }
        }
    }
}
