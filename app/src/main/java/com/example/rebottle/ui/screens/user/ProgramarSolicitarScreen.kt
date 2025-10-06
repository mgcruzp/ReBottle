package com.example.rebottle.ui.screens.home

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Business
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rebottle.R
import com.example.rebottle.ui.components.PrimaryButton
import java.util.*

private val Green = Color(0xFF1B4332)
private val Mint  = Color(0xFFDCFFD6)

@Composable
fun ProgramarSolicitudScreen() {
    val ctx = LocalContext.current
    var marca by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(20.dp)) {

        // Título centrado
        Text(
            text = "Programar recolección",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Green,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(Modifier.height(16.dp))

        // Marca (full width)
        OutlinedTextField(
            value = marca, onValueChange = { marca = it },
            label = { Text("Marca (Alpina, Postobón)") },
            leadingIcon = { Icon(Icons.Outlined.Business, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Green,
                unfocusedBorderColor = Mint,
                cursorColor = Green,
                focusedLabelColor = Green,
                focusedLeadingIconColor = Green
            )
        )

        Spacer(Modifier.height(12.dp))

        // Fecha (full width)
        OutlinedTextField(
            value = fecha, onValueChange = { },
            label = { Text("Fecha de recolección") },
            readOnly = true,
            leadingIcon = { Icon(Icons.Outlined.Event, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = {
                    val cal = Calendar.getInstance()
                    DatePickerDialog(
                        ctx,
                        { _, y, m, d -> fecha = "%02d/%02d/%04d".format(d, m + 1, y) },
                        cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH]
                    ).show()
                }) { Icon(Icons.Outlined.Event, contentDescription = "Elegir fecha") }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Green,
                unfocusedBorderColor = Mint,
                cursorColor = Green,
                focusedLabelColor = Green,
                focusedLeadingIconColor = Green
            )
        )

        Spacer(Modifier.height(12.dp))

        // Hora (full width)
        OutlinedTextField(
            value = hora, onValueChange = { },
            label = { Text("Hora") },
            readOnly = true,
            leadingIcon = { Icon(Icons.Outlined.Schedule, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = {
                    val cal = Calendar.getInstance()
                    TimePickerDialog(
                        ctx,
                        { _, h, min -> hora = "%02d:%02d".format(h, min) },
                        cal[Calendar.HOUR_OF_DAY], cal[Calendar.MINUTE], true
                    ).show()
                }) { Icon(Icons.Outlined.Schedule, contentDescription = "Elegir hora") }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Green,
                unfocusedBorderColor = Mint,
                cursorColor = Green,
                focusedLabelColor = Green,
                focusedLeadingIconColor = Green
            )
        )

        Spacer(Modifier.height(12.dp))

        // Ubicación (full width)
        OutlinedTextField(
            value = ubicacion, onValueChange = { ubicacion = it },
            label = { Text("Ubicación") },
            leadingIcon = { Icon(Icons.Outlined.LocationOn, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Green,
                unfocusedBorderColor = Mint,
                cursorColor = Green,
                focusedLabelColor = Green,
                focusedLeadingIconColor = Green
            )
        )

        // Imagen grande
        Spacer(Modifier.height(20.dp))
        Image(
            painter = painterResource(id = R.drawable.recoleccion),
            contentDescription = "Ilustración recolección",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        // Botón Confirmar
        Spacer(Modifier.height(20.dp))
        PrimaryButton(
            text = "Confirmar",
            enabled = marca.isNotBlank() && fecha.isNotBlank() && hora.isNotBlank() && ubicacion.isNotBlank(),
            onClick = { /* TODO: enviar solicitud */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        )
    }
}
