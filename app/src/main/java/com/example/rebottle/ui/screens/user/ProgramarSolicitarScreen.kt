package com.example.rebottle.ui.screens.home

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.rebottle.ui.components.PrimaryButton
import java.util.*

@Composable
fun ProgramarSolicitudScreen() {
    val ctx = LocalContext.current
    var marca by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Programar solicitud", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = marca, onValueChange = { marca = it },
            label = { Text("Marca (Alpina, Postobón)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                value = fecha, onValueChange = { },
                label = { Text("Fecha de recolección") },
                readOnly = true,
                modifier = Modifier.weight(1f),
                trailingIcon = {
                    TextButton(onClick = {
                        val cal = Calendar.getInstance()
                        DatePickerDialog(
                            ctx,
                            { _, y, m, d -> fecha = "%02d/%02d/%04d".format(d, m + 1, y) },
                            cal[Calendar.YEAR], cal[Calendar.MONTH], cal[Calendar.DAY_OF_MONTH]
                        ).show()
                    }) { Text("Elegir") }
                }
            )

            OutlinedTextField(
                value = hora, onValueChange = { },
                label = { Text("Hora") },
                readOnly = true,
                modifier = Modifier.weight(1f),
                trailingIcon = {
                    TextButton(onClick = {
                        val cal = Calendar.getInstance()
                        TimePickerDialog(
                            ctx,
                            { _, h, min -> hora = "%02d:%02d".format(h, min) },
                            cal[Calendar.HOUR_OF_DAY], cal[Calendar.MINUTE], true
                        ).show()
                    }) { Text("Elegir") }
                }
            )
        }
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = ubicacion, onValueChange = { ubicacion = it },
            label = { Text("Ubicación") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))
        PrimaryButton(
            text = "Confirmar",
            enabled = marca.isNotBlank() && fecha.isNotBlank() && hora.isNotBlank() && ubicacion.isNotBlank(),
            onClick = { /* TODO: enviar solicitud */ },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        )
    }
}
