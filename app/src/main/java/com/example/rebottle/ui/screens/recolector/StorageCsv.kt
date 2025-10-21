package com.example.rebottle.ui.screens.recolector

import android.content.Context
import java.io.File
import java.io.FileWriter
import java.io.IOException

object StorageCsv {
    private const val FILE_NAME = "entregas.csv"

    fun guardarEntrega(context: Context, entrega: ModelEntrega) {
        val file = File(context.getExternalFilesDir(null), FILE_NAME)
        val isNewFile = !file.exists()

        try {
            FileWriter(file, true).use { writer ->
                if (isNewFile) {
                    writer.append("Lugar,Peso,Foto,Fecha\n")
                }
                writer.append("${entrega.lugar},${entrega.peso},${entrega.fotoPath},${entrega.fecha}\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // 🔹 Recuperar entregas guardadas
    fun recuperarEntregas(context: Context): List<ModelEntrega> {
        val file = File(context.getExternalFilesDir(null), FILE_NAME)
        if (!file.exists()) return emptyList()

        return file.readLines()
            .drop(1) // Saltar encabezado
            .mapNotNull { line ->
                val parts = line.split(",")
                if (parts.size >= 4) {
                    ModelEntrega(
                        lugar = parts[0],
                        peso = parts[1].toFloatOrNull() ?: 0f,
                        fotoPath = parts[2],
                        fecha = parts[3]
                    )
                } else null
            }
    }
}
