package com.example.rebottle.domain.data

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: Role = Role.USUARIO
)
