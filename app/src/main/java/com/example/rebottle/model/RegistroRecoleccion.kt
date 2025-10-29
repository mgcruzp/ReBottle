package com.example.rebottle.model

data class RegistroRecoleccion(
    val id: String = "",
    val recolectorId: String = "",
    val recolectorNombre: String = "",
    val lugar: String = "",
    val peso: Double = 0.0,
    val fotoUri: String = "",
    val fecha: Long = System.currentTimeMillis()
)